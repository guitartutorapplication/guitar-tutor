package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.IGetSongsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.SongLibraryView;

import java.util.List;

/**
 * Presenter which provides the activities with all songs from the database API
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
            getSongsInteractor.getAllSongs(loggedInUser.getApiKey());
        } else {
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
