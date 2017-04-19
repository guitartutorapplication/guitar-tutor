package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API, setup selection and timer for beat preview
 */
public class PractiseSetupModel implements IPractiseSetupModel {

    private DatabaseApi api;
    private IPractiseSetupPresenter presenter;
    private Thread currentBeatPreview;
    private boolean requestStop;
    private SharedPreferences sharedPreferences;

    public PractiseSetupModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(IPractiseSetupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getChords() {
        // retrieving logged in user's id & api key from shared preferences
        final int userId = sharedPreferences.getInt("user_id", 0);
        final String apiKey = sharedPreferences.getString("api_key", "");
        Call<List<Chord>> userChordsCall = api.getUserChords(apiKey, userId);

        // asynchronously executing call
        userChordsCall.enqueue(new Callback<List<Chord>>() {
            @Override
            public void onResponse(Call<List<Chord>> call, Response<List<Chord>>
                    userChordsResponse) {
                if (userChordsResponse.isSuccessful()) {
                    List<Chord> userChords = userChordsResponse.body();
                    presenter.modelOnChordsRetrieved(userChords);
                }
                else {
                    presenter.modelOnLoadChordsError();
                }
            }

            @Override
            public void onFailure(Call<List<Chord>> call, Throwable t) {
                presenter.modelOnLoadChordsError();
            }
        });
    }

    @Override
    public void chordsSelected(List<Chord> selectedChords, int chordChangeIndex, int beatSpeedIndex) {
        // removing any null values (left on default option)
        List<Chord> chosenChords = new ArrayList<>();
        for (Chord chord : selectedChords) {
            if (chord != null) {
                chosenChords.add(chord);
            }
        }

        Set<Chord> uniqueChords = new HashSet<>(chosenChords);

        // if the user has selected less than two chords
        if (chosenChords.size() < 2) {
            presenter.modelOnLessThanTwoChordsSelected();
        }
        // if the user has selected the same chord more than once
        else if (uniqueChords.size() < chosenChords.size()) {
            presenter.modelOnSameSelectedChord();
        }
        else {
            presenter.modelOnCorrectSelectedChords(chosenChords,
                    ChordChange.values()[chordChangeIndex], BeatSpeed.values()[beatSpeedIndex]);
        }
    }

    @Override
    public void startBeatPreview(int beatSpeedIndex) {
        final BeatSpeed beatSpeed = BeatSpeed.values()[beatSpeedIndex];
        requestStop = false;

        Runnable timerTask = new Runnable() {
            final int totalLength = 4000;
            int currentLength = 0;

            @Override
            public void run() {
                // while timer has been running for less than 4 seconds
                while (totalLength - currentLength >= beatSpeed.getValue()) {
                    try {
                        if (requestStop) {
                            return;
                        }

                        // inform presenter every beat
                        presenter.modelOnNewBeat();
                        Thread.sleep(beatSpeed.getValue());
                        currentLength += beatSpeed.getValue();

                    } catch (InterruptedException e) {
                        presenter.modelOnPreviewBeatError();
                    }
                }

                presenter.modelOnBeatPreviewFinished();
            }
        };

        currentBeatPreview = new Thread(timerTask);
        currentBeatPreview.start();
    }

    @Override
    public void stopBeatPreview() {
        // stop if there is currently a beat preview running
        if (currentBeatPreview != null) {
            if (currentBeatPreview.isAlive()) {
                requestStop = true;
                presenter.modelOnBeatPreviewFinished();
            }
        }
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
