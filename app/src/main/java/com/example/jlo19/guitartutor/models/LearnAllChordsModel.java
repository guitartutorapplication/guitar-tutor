package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class LearnAllChordsModel implements ILearnAllChordsModel {

    private ILearnAllChordsPresenter presenter;
    private DatabaseApi api;
    private SharedPreferences sharedPreferences;
    private List<Chord> allChords;
    private List<Chord> userChords;
    private int userLevel;

    public LearnAllChordsModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(ILearnAllChordsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getChordsAndDetails() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // if all 3 API calls successfully retrieve the data needed
                if (setAllChords() && setUserChords() && setLevel()) {
                    presenter.modelOnChordsAndDetailsRetrieved();
                } else {
                    presenter.modelOnError();
                }
            }
        });
        thread.start();
    }

    private boolean setLevel() {
        // retrieving logged in user's id & api key from shared preferences
        final int userId = sharedPreferences.getInt("user_id", 0);
        final String apiKey = sharedPreferences.getString("api_key", "");
        // retrieving user's level (so know which chords are unlocked)
        try {
            Call<User> call = api.getAccountDetails(apiKey, userId);
            Response<User> response = call.execute();

            if (response.isSuccessful()) {
                userLevel = response.body().getLevel();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    private boolean setUserChords() {
        // retrieving logged in user's id & api key from shared preferences
        final int userId = sharedPreferences.getInt("user_id", 0);
        final String apiKey = sharedPreferences.getString("api_key", "");
        // retrieving chords that user has learnt
        try {
            Call<List<Chord>> call = api.getUserChords(apiKey, userId);
            Response<List<Chord>> response = call.execute();

            if (response.isSuccessful()) {
                userChords = response.body();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    private boolean setAllChords() {
        // retrieving logged in user's api key from shared preferences
        final String apiKey = sharedPreferences.getString("api_key", "");
        // retrieving all the chords
        try {
            Call<List<Chord>> call = api.getChords(apiKey);
            Response<List<Chord>> response = call.execute();

            if (response.isSuccessful()) {
                allChords = response.body();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public List<Chord> getAllChords() {
        return allChords;
    }

    @Override
    public int getUserLevel() {
        return userLevel;
    }

    @Override
    public List<Integer> getUserChordIds() {
        List<Integer> userChordIds = new ArrayList<>();
        for (Chord chord : userChords) {
            userChordIds.add(chord.getId());
        }
        return userChordIds;
    }
}
