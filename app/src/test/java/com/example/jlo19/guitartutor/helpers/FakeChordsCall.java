package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.Chord;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with list of Chord
 */
public class FakeChordsCall implements Call<List<Chord>> {

    private final Response<List<Chord>> response;

    public FakeChordsCall(Response<List<Chord>> response) {
        this.response = response;
    }

    @Override
    public Response<List<Chord>> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<List<Chord>> callback) {
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
    public Call<List<Chord>> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
