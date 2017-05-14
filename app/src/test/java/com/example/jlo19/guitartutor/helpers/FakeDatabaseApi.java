package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Fakes Database API calls
 */
public class FakeDatabaseApi implements DatabaseApi{

    private FakeUserCall fakeUserCall;
    private FakeMessageCall fakeMessageCall;
    private FakeChordsCall fakeChordsCall;
    private FakeSongsCall fakeSongsCall;

    public FakeDatabaseApi(FakeChordsCall fakeChordsCall) {
        this.fakeChordsCall = fakeChordsCall;
    }

    public FakeDatabaseApi(FakeSongsCall fakeSongsCall) {
        this.fakeSongsCall = fakeSongsCall;
    }

    public FakeDatabaseApi(FakeMessageCall fakeMessageCall) {
        this.fakeMessageCall = fakeMessageCall;
    }

    public FakeDatabaseApi(FakeUserCall fakeUserCall) {
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
    public Call<List<String>> registerUser(String name, String email, String password) {
        return fakeMessageCall;
    }

    @Override
    public Call<User> getAccountDetails(String apiKey, int userId) {
        return fakeUserCall;
    }

    @Override
    public Call<List<String>> editAccountDetails(
            String apiKey, int userId, String name, String email, String password) {
        return fakeMessageCall;
    }

    @Override
    public Call<User> loginUser(String email, String password) {
        return fakeUserCall;
    }

    @Override
    public Call<List<Chord>> getUserChords(String apiKey, int userId) {
        return fakeChordsCall;
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
