package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.Countdown;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.PostPutResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles timer for practise activity
 */
public class PractiseModel implements IPractiseModel {

    private List<Chord> selectedChords;
    private boolean requestStop;
    private IPractisePresenter presenter;
    private Runnable timerTask;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;
    private DatabaseApi api;
    private SharedPreferences sharedPreferences;
    private int numUpdateUserChordCalls;
    private boolean updateUserChordCallsSuccess;

    public PractiseModel(){
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setSelectedChords(List<Chord> selectedChords) {
        this.selectedChords = selectedChords;
    }

    @Override
    public void createPractiseTimer() {
        timerTask = new Runnable() {
            // round = one full run of all the chords
            int numRounds = 0;

            @Override
            public void run() {
                while (!requestStop) {
                    try {
                        for (int i = 0; i < selectedChords.size(); i++) {
                            // inform presenter that on the next chord in sequence
                            presenter.modelOnNewChord(selectedChords.get(i));

                            // play sound for number of beats in chord change
                            for (int j = 0; j < chordChange.getValue(); j++) {
                                if (requestStop) {
                                    return;
                                }
                                // inform presenter every beat
                                presenter.modelOnNewBeat();
                                Thread.sleep(beatSpeed.getValue());
                            }
                        }
                        numRounds++;
                        // after first round of chords
                        if (numRounds == 1) {
                            presenter.modelOnFirstRoundOfChords();
                        }
                    }
                    catch (InterruptedException e) {
                        presenter.modelOnError();
                    }
                }
            }
        };
    }

    @Override
    public void setPresenter(IPractisePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startPractiseTimer() {
        requestStop = false;
        new Thread(timerTask).start();
    }

    @Override
    public void stopTimer() {
        requestStop = true;
    }

    @Override
    public void setChordChange(ChordChange chordChange) {
        this.chordChange = chordChange;
    }

    @Override
    public void setBeatSpeed(BeatSpeed beatSpeed) {
        this.beatSpeed = beatSpeed;
    }

    @Override
    public void startCountdown() {
        requestStop = false;
        Runnable countdown = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 4; i++) {
                        if (requestStop) {
                            return;
                        }

                        // inform presenter every second of countdown
                        presenter.modelOnNewSecondOfCountdown(Countdown.values()[i]);
                        if (i == 3) {
                            Thread.sleep(3000);
                        }
                        else {
                            Thread.sleep(1500);
                        }
                    }
                }
                catch (InterruptedException e) {
                    presenter.modelOnError();
                }
                presenter.modelOnCountdownFinished();
            }
        };

        new Thread(countdown).start();
    }

    @Override
    public void savePractiseSession() {
        // retrieving logged in user's id from shared preferences
        int userId = sharedPreferences.getInt("user_id", 0);

        numUpdateUserChordCalls = 0;
        updateUserChordCallsSuccess = true;

        for (Chord chord : selectedChords) {
            Call<PostPutResponse> call = api.updateUserChord(userId, chord.getId());

            call.enqueue(new Callback<PostPutResponse>() {
                @Override
                public void onResponse(Call<PostPutResponse> call, Response<PostPutResponse> response) {
                    if (response.isSuccessful()) {
                        onUpdateUserChordCall(true);
                    }
                    else
                    {
                        onUpdateUserChordCall(false);
                    }
                }

                @Override
                public void onFailure(Call<PostPutResponse> call, Throwable t) {
                    onUpdateUserChordCall(false);
                }
            });
        }
    }

    private void onUpdateUserChordCall(boolean success) {
        numUpdateUserChordCalls++;

        // if call is not successfully, overall success is false
        if (!success) {
            updateUserChordCallsSuccess = false;
        }

        // if calls have been executed for all chords, notify presenter
        if (numUpdateUserChordCalls == selectedChords.size()) {
            int achievements = 0;
            if (updateUserChordCallsSuccess) {
                achievements = 100 * selectedChords.size();
            }
            presenter.modelOnPractiseSessionSaved(updateUserChordCallsSuccess, achievements);
        }
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
