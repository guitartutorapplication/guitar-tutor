package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.Song;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.models.retrofit.UserChord;
import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;
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
            Call<SongsResponse> call = api.getSongs();

            // asynchronously executing call
            call.enqueue(new Callback<SongsResponse>() {
                @Override
                public void onResponse(Call<SongsResponse> call, Response<SongsResponse> response) {
                    allSongs = response.body().getSongs();
                    presenter.modelOnSongsRetrieved(allSongs);
                }

                @Override
                public void onFailure(Call<SongsResponse> call, Throwable t) {
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
            Call<UserChordsResponse> userChordsCall = api.getUserChords(userId);

            // asynchronously executing call
            userChordsCall.enqueue(new Callback<UserChordsResponse>() {
                @Override
                public void onResponse(Call<UserChordsResponse> call, Response<UserChordsResponse>
                        userChordsResponse) {
                    // using the user chords to find which songs that user can play
                    List<UserChord> userChords = userChordsResponse.body().getUserChords();
                    songsUserCanPlay = new ArrayList<>();
                    for (Song song : allSongs) {
                        List<Chord> songChords = song.getChords();
                        boolean hasOnlyUserChords = true;
                        // comparing chords in song to chords the user knows
                        for (Chord songChord : songChords) {
                            boolean isAUserChord = false;
                            for (UserChord userChord : userChords) {
                                if (userChord.getChordId() == songChord.getId()) {
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
                    presenter.modelOnSongsRetrieved(songsUserCanPlay);
                }

                @Override
                public void onFailure(Call<UserChordsResponse> call, Throwable t) {
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
}
