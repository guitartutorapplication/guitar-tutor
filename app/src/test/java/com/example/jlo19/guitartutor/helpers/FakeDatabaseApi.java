package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.PostResponse;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import retrofit2.Call;

/**
 * Fakes Database API calls
 */
public class FakeDatabaseApi implements DatabaseApi{

    private FakePostResponseCall fakePostResponseCall;
    private FakeChordsResponseCall fakeChordsResponseCall;
    private FakeSongsResponseCall fakeSongsResponseCall;

    public FakeDatabaseApi(FakeChordsResponseCall fakeChordsResponseCall) {
        this.fakeChordsResponseCall = fakeChordsResponseCall;
    }

    public FakeDatabaseApi(FakeSongsResponseCall fakeSongsResponseCall) {
        this.fakeSongsResponseCall = fakeSongsResponseCall;
    }

    public FakeDatabaseApi(FakePostResponseCall fakePostResponseCall) {
        this.fakePostResponseCall = fakePostResponseCall;
    }

    @Override
    public Call<ChordsResponse> getChords() {
        return fakeChordsResponseCall;
    }

    @Override
    public Call<SongsResponse> getSongs() {
        return fakeSongsResponseCall;
    }

    @Override
    public Call<PostResponse> registerUser(String name, String email, String password) {
        return fakePostResponseCall;
    }

    @Override
    public Call<PostResponse> loginUser(String email, String password) {
        return fakePostResponseCall;
    }
}
