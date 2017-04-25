package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ISongModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;

import javax.inject.Inject;

/**
 * Handles connection to Amazon S3 API
 */
public class SongModel implements ISongModel {

    private IAmazonS3Service service;
    private ISongPresenter presenter;

    public SongModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void createAmazonS3Service(IAmazonS3Service service) {
        this.service = service;
        service.setUrlListener(this);
    }

    @Override
    public void setPresenter(ISongPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getAudio(String filename) {
        service.getUrl(filename);
    }

    @Override
    public void onUrlDownloadSuccess(String url) {
        presenter.modelOnUrlDownloadSuccess(url);
    }

    @Override
    public void onUrlDownloadFailed() {
        presenter.modelOnUrlDownloadFailed();
    }
}
