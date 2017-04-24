package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.listeners.GetChordsListener;
import com.example.jlo19.guitartutor.models.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class GetChordsInteractor implements IGetChordsInteractor {

    private GetChordsListener listener;
    private final DatabaseApi api;
    private List<Chord> allChords;
    private List<Chord> userChords;
    private int userLevel;

    public GetChordsInteractor(DatabaseApi api) {
        this.api = api;
    }

    public void setListener(GetChordsListener listener) {
        this.listener = listener;
    }

    @Override
    public void getChordsAndDetails(final String apiKey, final int userId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // if all 3 API calls successfully retrieve the data needed
                if (setAllChords(apiKey) && setUserChords(apiKey, userId) && setLevel(apiKey, userId)) {
                    listener.onChordsAndDetailsRetrieved(getAllChords(), getUserLevel(), getUserChordIds());
                } else {
                    listener.onError();
                }
            }
        });
        thread.start();
    }

    private boolean setLevel(String apiKey, int userId) {
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

    private boolean setUserChords(String apiKey, int userId) {
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

    private boolean setAllChords(String apiKey) {
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
    public List<Chord> getAllChords() {
        return allChords;
    }

    private int getUserLevel() {
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
