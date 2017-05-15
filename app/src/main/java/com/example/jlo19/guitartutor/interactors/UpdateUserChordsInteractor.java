package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.listeners.UpdateUserChordsListener;
import com.example.jlo19.guitartutor.interactors.interfaces.IUpdateUserChordsInteractor;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Deals with updating user chords on DB (API interaction)
 */
public class UpdateUserChordsInteractor implements IUpdateUserChordsInteractor {

    private final DatabaseApi api;
    private UpdateUserChordsListener listener;

    public UpdateUserChordsInteractor(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setListener(UpdateUserChordsListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateUserChords(String apiKey, int userId, ArrayList<Integer> chordIds) {
        Call<User> call = api.updateUserChords(apiKey, userId, chordIds);
        // asynchronously executing call
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    int level = response.body().getLevel();
                    int achievements = response.body().getAchievements();
                    listener.onUpdateUserChordsSuccess(level, achievements);
                }
                else {
                    listener.onUpdateUserChordsError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onUpdateUserChordsError();
            }
        });

    }
}
