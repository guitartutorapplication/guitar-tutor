package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
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
    private SharedPreferences sharedPreferences;

    @Override
    public void setView(IView view) {
        this.view = (SongLibraryView) view;
        this.view.showProgressBar();

        App.getComponent().inject(this);
    }

    @Inject
    void setModel(ISongLibraryModel model) {
        this.model = model;
        model.setPresenter(this);
        model.setSharedPreferences(sharedPreferences);
        model.getAllSongs();
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

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void viewOnSongFilterChanged(boolean viewAll) {
        if (viewAll) {
            model.getAllSongs();
        }
        else {
            model.getSongsUserCanPlay();
        }
    }

    @Override
    public void viewOnExit() {
        model.resetSongs();
    }
}
