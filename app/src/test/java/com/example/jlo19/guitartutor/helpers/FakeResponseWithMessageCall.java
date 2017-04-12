package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.responses.ResponseWithMessage;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fakes retrofit call with ResponseWithMessage
 */
public class FakeResponseWithMessageCall implements Call<ResponseWithMessage> {

    private final Response<ResponseWithMessage> response;

    public FakeResponseWithMessageCall(Response<ResponseWithMessage> response) {
        this.response = response;
    }

    @Override
    public Response<ResponseWithMessage> execute() throws IOException {
        if (response != null) {
            return response;
        }
        else {
            throw new IOException();
        }
    }

    @Override
    public void enqueue(Callback<ResponseWithMessage> callback) {
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
    public Call<ResponseWithMessage> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
