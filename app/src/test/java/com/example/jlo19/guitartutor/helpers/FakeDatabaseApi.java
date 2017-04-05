package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.LoginResponse;
import com.example.jlo19.guitartutor.models.retrofit.PostPutResponse;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.models.retrofit.User;
import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import retrofit2.Call;

/**
 * Fakes Database API calls
 */
public class FakeDatabaseApi implements DatabaseApi{

    private FakeUserChordsResponseCall fakeUserChordsResponseCall;
    private FakeUserCall fakeUserCall;
    private FakePostPutResponseCall fakePostPutResponseCall;
    private FakeChordsResponseCall fakeChordsResponseCall;
    private FakeSongsResponseCall fakeSongsResponseCall;
    private FakeLoginResponseCall fakeLoginResponseCall;

    public FakeDatabaseApi(FakeChordsResponseCall fakeChordsResponseCall) {
        this.fakeChordsResponseCall = fakeChordsResponseCall;
    }

    public FakeDatabaseApi(FakeSongsResponseCall fakeSongsResponseCall) {
        this.fakeSongsResponseCall = fakeSongsResponseCall;
    }

    public FakeDatabaseApi(FakePostPutResponseCall fakePostPutResponseCall) {
        this.fakePostPutResponseCall = fakePostPutResponseCall;
    }

    public FakeDatabaseApi(FakeLoginResponseCall fakeLoginResponseCall) {
        this.fakeLoginResponseCall = fakeLoginResponseCall;
    }

    public FakeDatabaseApi(FakeUserCall fakeUserCall) {
        this.fakeUserCall = fakeUserCall;
    }

    public FakeDatabaseApi(FakeChordsResponseCall fakeChordsResponseCall, FakeUserChordsResponseCall fakeUserChordsResponseCall) {
        this.fakeChordsResponseCall = fakeChordsResponseCall;
        this.fakeUserChordsResponseCall = fakeUserChordsResponseCall;
    }

    public FakeDatabaseApi(FakeSongsResponseCall fakeSongsResponseCall, FakeUserChordsResponseCall fakeUserChordsResponseCall) {
        this.fakeSongsResponseCall = fakeSongsResponseCall;
        this.fakeUserChordsResponseCall = fakeUserChordsResponseCall;
    }

    public FakeDatabaseApi(FakeUserChordsResponseCall fakeUserChordsResponseCall) {
        this.fakeUserChordsResponseCall = fakeUserChordsResponseCall;
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
    public Call<PostPutResponse> registerUser(String name, String email, String password) {
        return fakePostPutResponseCall;
    }

    @Override
    public Call<User> getAccountDetails(int userId) {
        return fakeUserCall;
    }

    @Override
    public Call<PostPutResponse> editAccountDetails(int userId, String name, String email, String password) {
        return fakePostPutResponseCall;
    }

    @Override
    public Call<LoginResponse> loginUser(String email, String password) {
        return fakeLoginResponseCall;
    }

    @Override
    public Call<UserChordsResponse> getUserChords(int userId) {
        return fakeUserChordsResponseCall;
    }

    @Override
    public Call<PostPutResponse> addUserChord(int userId, int chordId) {
        return fakePostPutResponseCall;
    }

    @Override
    public Call<PostPutResponse> updateUserChord(int userId, int chordId) {
        return fakePostPutResponseCall;
    }
}
