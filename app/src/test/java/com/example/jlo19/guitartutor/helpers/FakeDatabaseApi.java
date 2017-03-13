package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import retrofit2.Call;

/**
 * Fakes Database API calls
 */
public class FakeDatabaseApi implements DatabaseApi{

    private final FakeCall fakeCall;

    public FakeDatabaseApi(FakeCall fakeCall) {
        this.fakeCall = fakeCall;
    }

    @Override
    public Call<ChordsResponse> getChords() {
        return fakeCall;
    }
}
