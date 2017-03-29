package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.LoginResponse;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with LoginResponse
 */
public class FakeLoginResponseCall implements Call<LoginResponse> {

    private final Response<LoginResponse> response;

    public FakeLoginResponseCall(Response<LoginResponse> response) {
        this.response = response;
    }

    @Override
    public Response<LoginResponse> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<LoginResponse> callback) {
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
    public Call<LoginResponse> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
