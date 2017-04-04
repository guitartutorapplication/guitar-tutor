package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LearnChordView;

import javax.inject.Inject;

/**
 * Presenter which provides the LearnChordActivity with data from the amazon s3 API
 */
public class LearnChordPresenter implements ILearnChordPresenter {

    private LearnChordView view;
    private ILearnChordModel model;
    private SharedPreferences sharedPreferences;

    @Override
    public void setView(IView view) {
        this.view = (LearnChordView) view;
        this.view.showProgressBar();
        this.view.enableLearntButton(!this.view.getLearntChord());

        App.getComponent().inject(this);
    }

    @Inject
    void setModel(ILearnChordModel model) {
        this.model = model;
        model.setPresenter(this);
        model.setContext(view.getContext());
        model.getImage(view.getChord().getDiagramFilename());
        model.setSharedPreferences(sharedPreferences);
    }

    @Override
    public void viewOnVideoRequested() {
        view.showProgressBar();
        model.getVideo(view.getChord().getVideoFilename());
    }

    @Override
    public void modelOnImageDownloadFailed() {
        view.hideProgressBar();
        view.showImageLoadError();
    }

    @Override
    public void modelOnImageDownloadSuccess(Bitmap bitmap) {
        view.hideProgressBar();
        view.setImage(bitmap);
    }

    @Override
    public void modelOnVideoDownloadSuccess(String url) {
        view.hideProgressBar();
        view.playVideo(url);
    }

    @Override
    public void modelOnVideoDownloadFailed() {
        view.hideProgressBar();
        view.showVideoLoadError();
    }

    @Override
    public void viewOnLearnt() {
        view.showConfirmDialog();
    }

    @Override
    public void viewOnConfirmLearnt() {
        view.showProgressBar();
        model.addLearntChord(view.getChord().getId());
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void modelOnLearntChordAdded() {
        view.hideProgressBar();
        view.showAddLearntChordSuccess();
        view.startLearnAllChordsActivity();
    }

    @Override
    public void modelOnAddLearntChordError() {
        view.hideProgressBar();
        view.showAddLearntChordError();
    }

    @Override
    public void modelOnUpdateLevelDetailsError() {
        view.showUpdateLevelDetailsError();
    }
}
