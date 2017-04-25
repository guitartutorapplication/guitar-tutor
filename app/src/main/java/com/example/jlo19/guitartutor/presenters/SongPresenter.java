package com.example.jlo19.guitartutor.presenters;

import android.view.View;

import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.SongView;

/**
 * Presenter which provides activity with capability to retrieve song from S3
 */
public class SongPresenter implements ISongPresenter {
    private final IAmazonS3Service amazonS3Service;
    private SongView view;

    public SongPresenter(IAmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
        this.amazonS3Service.setUrlListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (SongView)view;
    }

    @Override
    public void viewOnPlay() {
        view.showProgressBar();
        amazonS3Service.getUrl(view.getAudioFilename());
    }

    @Override
    public void onUrlDownloadSuccess(String url) {
        view.playAudio(url);
    }

    @Override
    public void onUrlDownloadFailed() {
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
