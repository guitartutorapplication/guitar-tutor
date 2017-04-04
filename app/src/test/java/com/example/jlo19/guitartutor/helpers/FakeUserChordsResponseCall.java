package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with UserChordsResponse
 */
public class FakeUserChordsResponseCall implements Call<UserChordsResponse> {

    private final Response<UserChordsResponse> response;

    public FakeUserChordsResponseCall(Response<UserChordsResponse> response) {
        this.response = response;
    }

    @Override
    public Response<UserChordsResponse> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<UserChordsResponse> callback) {
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
    public Call<UserChordsResponse> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
