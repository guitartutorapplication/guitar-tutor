package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.Song;

import java.util.List;

/**
 * Interface for SongLibraryPresenter
 */
public interface ISongLibraryPresenter extends IPresenter {
    void modelOnSongsRetrieved(List<Song> songs);
    void modelOnError();
}
