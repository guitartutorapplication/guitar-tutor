package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;

/**
 * Interface for SongLibraryModel
 */
public interface ISongLibraryModel {
    void setPresenter(ISongLibraryPresenter presenter);
    void getSongs();
}