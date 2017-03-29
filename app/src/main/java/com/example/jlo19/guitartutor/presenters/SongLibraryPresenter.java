package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.SongLibraryView;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter which provides the activities with all songs from the database API
 */
public class SongLibraryPresenter implements ISongLibraryPresenter {

    private SongLibraryView view;
    private ISongLibraryModel model;

    public SongLibraryPresenter() {
        App.getComponent().inject(this);}

    @Override
    public void setView(IView view) {
        this.view = (SongLibraryView) view;
        this.view.showProgressBar();

        model.getSongs();
    }

    @Inject
    void setModel(ISongLibraryModel model) {
        this.model = model;
        model.setPresenter(this);
    }

    @Override
    public void modelOnSongsRetrieved(List<Song> songs) {
        view.hideProgressBar();
        view.setSongs(songs);
    }

    @Override
    public void modelOnError() {
        view.hideProgressBar();
        view.showError();
    }
}
