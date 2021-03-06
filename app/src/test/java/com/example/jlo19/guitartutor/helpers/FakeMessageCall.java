package com.example.jlo19.guitartutor.helpers;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with list of String
 */
public class FakeMessageCall implements Call<List<String>> {

    private final Response<List<String>> response;

    public FakeMessageCall(Response<List<String>> response) {
        this.response = response;
    }

    @Override
    public Response<List<String>> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<List<String>> callback) {
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
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<List<String>> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
