package com.example.jlo19.guitartutor.presenters;

import android.view.View;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ISongModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.SongView;

import javax.inject.Inject;

/**
 * Presenter which provides activity with capability to retrieve song from S3
 */
public class SongPresenter implements ISongPresenter {
    private SongView view;
    private ISongModel model;

    @Inject
    void setModel(ISongModel model) {
        this.model = model;
        model.setPresenter(this);
        model.setContext(view.getContext());
    }

    @Override
    public void setView(IView view) {
        this.view = (SongView)view;
        App.getComponent().inject(this);
    }

    @Override
    public void viewOnPlay() {
        view.showProgressBar();
        model.getAudio(view.getAudioFilename());
    }

    @Override
    public void modelOnUrlDownloadSuccess(String url) {
        view.playAudio(url);
    }

    @Override
    public void modelOnUrlDownloadFailed() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void viewOnAudioLoadFailed() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void viewOnAudioLoaded() {
        view.hideProgressBar();
        view.setStopButtonVisibility(View.VISIBLE);
        view.setPlayButtonVisibility(View.INVISIBLE);
    }

    @Override
    public void viewOnStop() {
        view.setPlayButtonVisibility(View.VISIBLE);
        view.setStopButtonVisibility(View.INVISIBLE);
        view.stopAudio();
    }
}
