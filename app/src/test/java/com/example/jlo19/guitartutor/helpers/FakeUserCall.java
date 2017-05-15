package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.User;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with User
 */
public class FakeUserCall implements Call<User> {

    private final Response<User> response;

    public FakeUserCall(Response<User> response) {
        this.response = response;
    }

    @Override
    public Response<User> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<User> callback) {
        // no response has been set, on failure result
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
    public void cancel() {}

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<User> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
