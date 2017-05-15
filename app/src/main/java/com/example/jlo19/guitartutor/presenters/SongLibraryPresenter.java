package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetSongsInteractor;
import com.example.jlo19.guitartutor.models.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.SongLibraryView;

import java.util.List;

/**
 * Presenter that provides SongLibraryActivity with DB API interaction
 */
public class SongLibraryPresenter implements ISongLibraryPresenter {

    private SongLibraryView view;
    private final IGetSongsInteractor getSongsInteractor;
    private final LoggedInUser loggedInUser;

    public SongLibraryPresenter(IGetSongsInteractor getSongsInteractor, LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.getSongsInteractor = getSongsInteractor;
        this.getSongsInteractor.setListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (SongLibraryView) view;
        this.view.showProgressBar();

        // get all songs from DB
        this.getSongsInteractor.getAllSongs(loggedInUser.getApiKey());
    }

    @Override
    public void onSongsRetrieved(List<Song> songs) {
        view.hideProgressBar();
        view.setSongs(songs);
    }

    @Override
    public void onError() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void viewOnSongFilterChanged(boolean viewAll) {
        if (viewAll) {
            // if filter is change to view all songs, retrieve all from DB
            getSongsInteractor.getAllSongs(loggedInUser.getApiKey());
        } else {
            // retrieve songs user can play from DB
            getSongsInteractor.getSongsUserCanPlay(loggedInUser.getApiKey(), loggedInUser.getUserId());
        }
    }

    @Override
    public void viewOnExit() {
        getSongsInteractor.resetSongs();
    }

    @Override
    public void viewOnConfirmError() {
        view.finishActivity();
    }

}
