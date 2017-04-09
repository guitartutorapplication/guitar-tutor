package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.models.retrofit.objects.Song;

import java.util.List;

/**
 * Interface for SongLibraryPresenter
 */
public interface ISongLibraryPresenter extends IPresenter {
    void modelOnSongsRetrieved(List<Song> songs);
    void modelOnError();
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void viewOnSongFilterChanged(boolean viewAll);
    void viewOnExit();
}
