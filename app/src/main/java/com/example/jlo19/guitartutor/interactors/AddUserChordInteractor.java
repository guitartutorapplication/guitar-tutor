package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.listeners.AddUserChordListener;
import com.example.jlo19.guitartutor.interactors.interfaces.IAddUserChordInteractor;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Deals with adding user chord to DB (API interaction)
 */
public class AddUserChordInteractor implements IAddUserChordInteractor {

    private AddUserChordListener listener;
    private final DatabaseApi api;

    public AddUserChordInteractor(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setListener(AddUserChordListener listener) {
        this.listener = listener;
    }

    @Override
    public void addUserChord(String apiKey, int userId, int chordId) {
        Call<User> call = api.addUserChord(apiKey, userId, chordId);

        // asynchronously executing call
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    int level = response.body().getLevel();
                    int achievements = response.body().getAchievements();
                    listener.onChordAdded(level, achievements);
                }
                else {
                    listener.onAddChordError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onAddChordError();
            }
        });
    }
}
