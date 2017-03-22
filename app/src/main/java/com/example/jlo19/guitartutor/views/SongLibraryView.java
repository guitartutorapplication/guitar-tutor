package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.retrofit.Song;

import java.util.List;

/**
 * View interface for SongLibraryActivity
 */
public interface SongLibraryView extends IView {
    void setToolbarTitleText();
    void showProgressBar();
    void hideProgressBar();
    void setSongs(List<Song> songs);
    void showError();
}
