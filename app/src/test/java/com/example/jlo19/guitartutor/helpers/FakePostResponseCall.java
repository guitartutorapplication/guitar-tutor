package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.PostResponse;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with PostResponse
 */
public class FakePostResponseCall implements Call<PostResponse> {

    private final Response<PostResponse> response;

    public FakePostResponseCall(Response<PostResponse> response) {
        this.response = response;
    }

    @Override
    public Response<PostResponse> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<PostResponse> callback) {
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
    public Call<PostResponse> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
