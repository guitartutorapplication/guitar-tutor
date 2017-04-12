package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.objects.Song;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with SongsResponse
 */
public class FakeSongsCall implements Call<List<Song>> {

    private final Response<List<Song>> response;

    public FakeSongsCall(Response<List<Song>> response) {
        this.response = response;
    }

    @Override
    public Response<List<Song>> execute() throws IOException {
        if (response != null) {
            return response;
        }
        else {
            throw new IOException();
        }
    }

    @Override
    public void enqueue(Callback<List<Song>> callback) {
        if (response != null) {
            callback.onResponse(this, response);
        }
        else {
            callback.onFailure(this, null);
        }
    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<List<Song>> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
