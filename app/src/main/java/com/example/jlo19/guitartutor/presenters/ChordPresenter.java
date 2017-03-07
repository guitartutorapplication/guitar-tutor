package com.example.jlo19.guitartutor.presenters;

import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.services.AmazonS3Service;
import com.example.jlo19.guitartutor.views.ChordView;

import java.net.URL;

import javax.inject.Inject;

/**
 * Presenter which provides the ChordActivity with data from the amazon s3 API
 */
public class ChordPresenter implements AmazonS3ServiceListener {

    private ChordView view;
    private AmazonS3Service service;

    public void setView(ChordView view) {
        this.view = view;

        view.showProgressBar();

        App.getComponent().inject(this);
    }

    @Inject
    void createAmazonS3Service(AmazonS3Service service) {
        this.service = service;
        service.setListener(this);
        service.setClient(view.getContext());
        service.getImage(view.getChord().getDiagramFilename());
    }

    @Override
    public void onDownloadFailed() {
        // if task fails to retrieve image, report failure
        view.showError();
    }

    @Override
    public void onDownloadSuccess(Bitmap bitmap) {
        // if task successfully retrieves image, send back image
        view.hideProgressBar();
        view.setImage(bitmap);
    }

    @Override
    public void onDownloadSuccess(URL url) {
        // if task successfully retrieves URL, send back URL
        view.hideProgressBar();
        view.playVideo(url);
    }

    public void getVideo() {
        service.getVideo(view.getChord().getVideoFilename());
    }
}
