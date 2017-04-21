package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.retrofit.objects.Song;

import java.util.List;

/**
 * View interface for SongLibraryActivity
 */
public interface SongLibraryView extends IProgressBarView {
    void setSongs(List<Song> songs);
    void showError();
    void finishActivity();
}
