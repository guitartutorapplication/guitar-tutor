package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class SongLibraryModel implements ISongLibraryModel {

    private ISongLibraryPresenter presenter;
    private DatabaseApi api;
    private List<Song> allSongs;
    private List<Song> songsUserCanPlay;
    private SharedPreferences sharedPreferences;

    public SongLibraryModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(ISongLibraryPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getAllSongs() {
        if (allSongs == null) {
            Call<List<Song>> call = api.getSongs();

            // asynchronously executing call
            call.enqueue(new Callback<List<Song>>() {
                @Override
                public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                    if (response.isSuccessful()) {
                        allSongs = response.body();
                        presenter.modelOnSongsRetrieved(allSongs);
                    }
                    else {
                        presenter.modelOnError();
                    }
                }

                @Override
                public void onFailure(Call<List<Song>> call, Throwable t) {
                    presenter.modelOnError();
                }
            });
        }
        else {
            presenter.modelOnSongsRetrieved(allSongs);
        }
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void getSongsUserCanPlay() {
        if (songsUserCanPlay == null) {
            // retrieving logged in user's id from shared preferences
            final int userId = sharedPreferences.getInt("user_id", 0);
            Call<List<Chord>> userChordsCall = api.getUserChords(userId);

            // asynchronously executing call
            userChordsCall.enqueue(new Callback<List<Chord>>() {
                @Override
                public void onResponse(Call<List<Chord>> call, Response<List<Chord>>
                        userChordsResponse) {
                    if (userChordsResponse.isSuccessful()) {
                        List<Chord> userChords = userChordsResponse.body();
                        setSongsUserCanPlay(userChords);
                        presenter.modelOnSongsRetrieved(songsUserCanPlay);
                    }
                    else {
                        presenter.modelOnError();
                    }
                }

                @Override
                public void onFailure(Call<List<Chord>> call, Throwable t) {
                    presenter.modelOnError();
                }
            });
        }
        else {
            presenter.modelOnSongsRetrieved(songsUserCanPlay);
        }
    }

    @Override
    public void resetSongs() {
        songsUserCanPlay = null;
        allSongs = null;
    }

    private void setSongsUserCanPlay(List<Chord> userChords) {
        // using the user chords to find which songs that user can play
        songsUserCanPlay = new ArrayList<>();
        for (Song song : allSongs) {
            List<Chord> songChords = song.getChords();
            boolean hasOnlyUserChords = true;
            // comparing chords in song to chords the user knows
            for (Chord songChord : songChords) {
                boolean isAUserChord = false;
                for (Chord userChord : userChords) {
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
    }
}
