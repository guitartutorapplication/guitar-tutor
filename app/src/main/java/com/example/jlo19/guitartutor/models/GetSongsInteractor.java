package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.listeners.GetSongsListener;
import com.example.jlo19.guitartutor.models.interfaces.IGetSongsInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class GetSongsInteractor implements IGetSongsInteractor {

    private GetSongsListener listener;
    private final DatabaseApi api;
    private final IGetUserChordsInteractor getUserChordsInteractor;
    private List<Song> allSongs;
    private List<Song> songsUserCanPlay;

    public GetSongsInteractor(DatabaseApi api, IGetUserChordsInteractor getUserChordsInteractor) {
        this.api = api;
        this.getUserChordsInteractor = getUserChordsInteractor;
    }

    @Override
    public void setListener(GetSongsListener listener) {
        this.listener = listener;
    }

    @Override
    public void getAllSongs(String apiKey) {
        if (allSongs == null) {
            Call<List<Song>> call = api.getSongs(apiKey);

            // asynchronously executing call
            call.enqueue(new Callback<List<Song>>() {
                @Override
                public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                    if (response.isSuccessful()) {
                        allSongs = response.body();
                        listener.onSongsRetrieved(allSongs);
                    }
                    else {
                        listener.onError();
                    }
                }

                @Override
                public void onFailure(Call<List<Song>> call, Throwable t) {
                    listener.onError();
                }
            });
        }
        else {
            listener.onSongsRetrieved(allSongs);
        }
    }

    public void getSongsUserCanPlay(String apiKey, int userId) {
        if (songsUserCanPlay == null) {
            getUserChordsInteractor.setListener(this);
            getUserChordsInteractor.getUserChords(apiKey, userId);
        }
        else {
            listener.onSongsRetrieved(songsUserCanPlay);
        }
    }

    @Override
    public void resetSongs() {
        songsUserCanPlay = null;
        allSongs = null;
    }

    @Override
    public void onUserChordsRetrieved(List<Chord> chords) {
        // using the user chords to find which songs that user can play
        songsUserCanPlay = new ArrayList<>();
        for (Song song : allSongs) {
            List<Chord> songChords = song.getChords();
            boolean hasOnlyUserChords = true;
            // comparing chords in song to chords the user knows
            for (Chord songChord : songChords) {
                boolean isAUserChord = false;
                for (Chord userChord : chords) {
                    if (userChord.getId() == songChord.getId()) {
                        isAUserChord = true;
                        break;
                    }
                }
                if (!isAUserChord) {
                    hasOnlyUserChords = false;
                    break;
                }
            }
            if (hasOnlyUserChords) {
                songsUserCanPlay.add(song);
            }
        }

        listener.onSongsRetrieved(songsUserCanPlay);
    }

    @Override
    public void onError() {
        listener.onError();
    }
}
