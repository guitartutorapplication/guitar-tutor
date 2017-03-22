package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with ChordsResponse
 */
public class FakeChordsResponseCall implements Call<ChordsResponse> {

    private final Response<ChordsResponse> response;

    public FakeChordsResponseCall(Response<ChordsResponse> response) {
        this.response = response;
    }

    @Override
    public Response<ChordsResponse> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<ChordsResponse> callback) {
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
    public Call<ChordsResponse> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
