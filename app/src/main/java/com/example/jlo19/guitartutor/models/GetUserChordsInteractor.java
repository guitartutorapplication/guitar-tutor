package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.listeners.GetUserChordsListener;
import com.example.jlo19.guitartutor.models.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to DB API
 */
public class GetUserChordsInteractor implements IGetUserChordsInteractor {

    private final DatabaseApi api;
    private GetUserChordsListener listener;

    public GetUserChordsInteractor(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setListener(GetUserChordsListener listener) {
        this.listener = listener;
    }

    @Override
    public void getUserChords(String apiKey, int userId) {
        Call<List<Chord>> call = api.getUserChords(apiKey, userId);
        // asynchronously executing call
        call.enqueue(new Callback<List<Chord>>() {
            @Override
            public void onResponse(Call<List<Chord>> call, Response<List<Chord>> response) {
                if (response.isSuccessful()) {
                    listener.onUserChordsRetrieved(response.body());
                }
                else {
                    listener.onGetUserChordsError();
                }
            }

            @Override
            public void onFailure(Call<List<Chord>> call, Throwable t) {
                listener.onGetUserChordsError();
            }
        });
    }
}
