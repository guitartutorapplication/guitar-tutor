package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.RegisterResponse;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with RegisterResponse
 */
public class FakeRegisterResponseCall implements Call<RegisterResponse> {

    private final Response<RegisterResponse> response;

    public FakeRegisterResponseCall(Response<RegisterResponse> response) {
        this.response = response;
    }

    @Override
    public Response<RegisterResponse> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<RegisterResponse> callback) {
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
    public Call<RegisterResponse> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
