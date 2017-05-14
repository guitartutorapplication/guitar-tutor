package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.listeners.GetChordsListener;
import com.example.jlo19.guitartutor.models.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class GetChordsInteractor implements IGetChordsInteractor {

    private final IGetUserChordsInteractor getUserChordsInteractor;
    private final IGetAccountDetailsInteractor getAccountDetailsInteractor;
    private GetChordsListener listener;
    private final DatabaseApi api;
    private List<Chord> allChords;
    private List<Chord> userChords;
    private String apiKey;
    private int userId;

    public GetChordsInteractor(DatabaseApi api, IGetUserChordsInteractor getUserChordsInteractor,
            IGetAccountDetailsInteractor getAccountDetailsInteractor) {
        this.getUserChordsInteractor = getUserChordsInteractor;
        this.getUserChordsInteractor.setListener(this);
        this.getAccountDetailsInteractor = getAccountDetailsInteractor;
        this.getAccountDetailsInteractor.setListener(this);
        this.api = api;
    }

    public void setListener(GetChordsListener listener) {
        this.listener = listener;
    }

    @Override
    public void getChordsAndDetails(final String apiKey, final int userId) {
        this.apiKey = apiKey;
        this.userId = userId;
        // retrieving all the chords
        Call<List<Chord>> call = api.getChords(apiKey);

        call.enqueue(new Callback<List<Chord>>() {
            @Override
            public void onResponse(Call<List<Chord>> call, Response<List<Chord>> response) {
                if (response.isSuccessful()) {
                    allChords = response.body();
                    // retrieving chords that user has learnt
                    getUserChordsInteractor.getUserChords(apiKey, userId);
                }
                else {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<List<Chord>> call, Throwable t) {
                listener.onError();
            }
        });
    }

    @Override
    public List<Chord> getAllChords() {
        return allChords;
    }


    @Override
    public List<Integer> getUserChordIds() {
        List<Integer> userChordIds = new ArrayList<>();
        for (Chord chord : userChords) {
            userChordIds.add(chord.getId());
        }
        return userChordIds;
    }

    @Override
    public void onAccountDetailsRetrieved(User user) {
        listener.onChordsAndDetailsRetrieved(allChords, user.getLevel(), getUserChordIds());
    }

    @Override
    public void onGetAccountDetailsError() {
        listener.onError();
    }

    @Override
    public void onUserChordsRetrieved(List<Chord> chords) {
        userChords = chords;
        // retrieving user's level (so know which chords are unlocked)
        getAccountDetailsInteractor.getAccountDetails(apiKey, userId);
    }

    @Override
    public void onGetUserChordsError() {
        listener.onError();
    }
}
