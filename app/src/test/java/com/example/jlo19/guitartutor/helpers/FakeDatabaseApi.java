package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.LoginResponse;
import com.example.jlo19.guitartutor.models.retrofit.PostResponse;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.models.retrofit.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import retrofit2.Call;

/**
 * Fakes Database API calls
 */
public class FakeDatabaseApi implements DatabaseApi{

    private FakeUserCall fakeUserCall;
    private FakePostResponseCall fakePostResponseCall;
    private FakeChordsResponseCall fakeChordsResponseCall;
    private FakeSongsResponseCall fakeSongsResponseCall;
    private FakeLoginResponseCall fakeLoginResponseCall;

    public FakeDatabaseApi(FakeChordsResponseCall fakeChordsResponseCall) {
        this.fakeChordsResponseCall = fakeChordsResponseCall;
    }

    public FakeDatabaseApi(FakeSongsResponseCall fakeSongsResponseCall) {
        this.fakeSongsResponseCall = fakeSongsResponseCall;
    }

    public FakeDatabaseApi(FakePostResponseCall fakePostResponseCall) {
        this.fakePostResponseCall = fakePostResponseCall;
    }

    public FakeDatabaseApi(FakeLoginResponseCall fakeLoginResponseCall) {
        this.fakeLoginResponseCall = fakeLoginResponseCall;
    }

    public FakeDatabaseApi(FakeUserCall fakeUserCall) {
        this.fakeUserCall = fakeUserCall;
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
    public Call<User> getAccountDetails(int userId) {
        return fakeUserCall;
    }

    @Override
    public Call<LoginResponse> loginUser(String email, String password) {
        return fakeLoginResponseCall;
    }
}
