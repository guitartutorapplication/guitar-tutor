package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.GetSongsListener;

/**
 * Interface for SongLibraryPresenter
 */
public interface ISongLibraryPresenter extends IPresenter, GetSongsListener {
    void viewOnSongFilterChanged(boolean viewAll);
    void viewOnExit();
    void viewOnConfirmError();
}
