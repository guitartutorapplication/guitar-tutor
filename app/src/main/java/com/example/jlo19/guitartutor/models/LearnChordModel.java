package com.example.jlo19.guitartutor.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;

import javax.inject.Inject;

/**
 * Handles connection to Amazon S3 API
 */
public class LearnChordModel implements ILearnChordModel {

    private IAmazonS3Service service;
    private ILearnChordPresenter presenter;

    public LearnChordModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void createAmazonS3Service(IAmazonS3Service service) {
        this.service = service;
        service.setListener(this);
    }

    @Override
    public void onImageDownloadFailed() {
        presenter.modelOnImageDownloadFailed();
    }

    @Override
    public void onImageDownloadSuccess(Bitmap bitmap) {
        presenter.modelOnImageDownloadSuccess(bitmap);
    }

    @Override
    public void onVideoDownloadSuccess(String url) {
        presenter.modelOnVideoDownloadSuccess(url);
    }

    @Override
    public void onVideoDownloadFailed() {
        presenter.modelOnVideoDownloadFailed();
    }

    @Override
    public void setContext(Context context) {
        service.setClient(context);
    }

    @Override
    public void getImage(String filename) {
        service.getImage(filename);
    }

    @Override
    public void setPresenter(ILearnChordPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getVideo(String filename) {
        service.getVideo(filename);
    }
}
