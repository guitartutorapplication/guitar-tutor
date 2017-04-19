package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.models.retrofit.responses.ResponseWithMessage;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Fakes Database API calls
 */
public class FakeDatabaseApi implements DatabaseApi{

    private FakeChordsCall fakeUserChordsResponseCall;
    private FakeUserCall fakeUserCall;
    private FakeResponseWithMessageCall fakeResponseWithMessageCall;
    private FakeChordsCall fakeChordsCall;
    private FakeSongsCall fakeSongsCall;

    public FakeDatabaseApi(FakeChordsCall fakeChordsCall) {
        this.fakeChordsCall = fakeChordsCall;
    }

    public FakeDatabaseApi(FakeSongsCall fakeSongsCall) {
        this.fakeSongsCall = fakeSongsCall;
    }

    public FakeDatabaseApi(FakeResponseWithMessageCall fakeResponseWithMessageCall) {
        this.fakeResponseWithMessageCall = fakeResponseWithMessageCall;
    }

    public FakeDatabaseApi(FakeUserCall fakeUserCall) {
        this.fakeUserCall = fakeUserCall;
    }

    public FakeDatabaseApi(FakeSongsCall fakeSongsCall, FakeChordsCall
            fakeUserChordsResponseCall) {
        this.fakeSongsCall = fakeSongsCall;
        this.fakeUserChordsResponseCall = fakeUserChordsResponseCall;
    }

    public FakeDatabaseApi(FakeChordsCall fakeChordsCall, FakeChordsCall
            fakeUserChordsResponseCall, FakeUserCall fakeUserCall) {
        this.fakeChordsCall = fakeChordsCall;
        this.fakeUserChordsResponseCall = fakeUserChordsResponseCall;
        this.fakeUserCall = fakeUserCall;
    }

    @Override
    public Call<List<Chord>> getChords(String apiKey) {
        return fakeChordsCall;
    }

    @Override
    public Call<List<Song>> getSongs(String apiKey) {
        return fakeSongsCall;
    }

    @Override
    public Call<ResponseWithMessage> registerUser(String name, String email, String password) {
        return fakeResponseWithMessageCall;
    }

    @Override
    public Call<User> getAccountDetails(String apiKey, int userId) {
        return fakeUserCall;
    }

    @Override
    public Call<ResponseWithMessage> editAccountDetails(
            String apiKey, int userId, String name, String email, String password) {
        return fakeResponseWithMessageCall;
    }

    @Override
    public Call<User> loginUser(String email, String password) {
        return fakeUserCall;
    }

    @Override
    public Call<List<Chord>> getUserChords(String apiKey, int userId) {
        if (fakeUserChordsResponseCall == null) {
            return fakeChordsCall;
        }
        else {
            return fakeUserChordsResponseCall;
        }
    }

    @Override
    public Call<User> addUserChord(String apiKey, int userId, int chordId) {
        return fakeUserCall;
    }

    @Override
    public Call<User> updateUserChords(String apiKey, int userId, ArrayList<Integer> chordIds) {
        return fakeUserCall;
    }
}
