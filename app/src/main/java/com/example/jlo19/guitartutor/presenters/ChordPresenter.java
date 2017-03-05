package com.example.jlo19.guitartutor.presenters;

import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.services.AmazonS3Service;
import com.example.jlo19.guitartutor.views.ChordView;

/**
 * Presenter which provides the ChordActivity with data from the amazon s3 API
 */
public class ChordPresenter implements AmazonS3ServiceListener {

    private ChordView view;

    public void setView(ChordView view) {
        this.view = view;
        AmazonS3Service service = new AmazonS3Service(view.getContext(), this);

        view.showProgressBar();
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
}
