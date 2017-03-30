package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.PostPutResponse;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with PostPutResponse
 */
public class FakePostPutResponseCall implements Call<PostPutResponse> {

    private final Response<PostPutResponse> response;

    public FakePostPutResponseCall(Response<PostPutResponse> response) {
        this.response = response;
    }

    @Override
    public Response<PostPutResponse> execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback<PostPutResponse> callback) {
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
    public Call<PostPutResponse> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
