package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.Song;

import java.util.List;

/**
 * View interface for SongLibraryActivity
 */
public interface SongLibraryView extends ProgressBarView {
    void setSongs(List<Song> songs);
    void showError();
    void finishActivity();
}
