package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with SongsResponse
 */
public class FakeSongsResponseCall implements Call<SongsResponse> {

    private final Response<SongsResponse> response;

    public FakeSongsResponseCall(Response<SongsResponse> response) {
        this.response = response;
    }

    @Override
    public Response<SongsResponse> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<SongsResponse> callback) {
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
    public Call<SongsResponse> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
