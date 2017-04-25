package com.example.jlo19.guitartutor.presenters;

import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.IAddUserChordInteractor;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LearnChordView;

/**
 * Presenter which provides the LearnChordActivity with data from the amazon s3 API
 */
public class LearnChordPresenter implements ILearnChordPresenter {

    private final IAmazonS3Service amazonS3Service;
    private LearnChordView view;
    private final IAddUserChordInteractor addUserChordInteractor;
    private final LoggedInUser loggedInUser;

    public LearnChordPresenter(IAddUserChordInteractor addUserChordInteractor, IAmazonS3Service
            amazonS3Service, LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;

        this.addUserChordInteractor = addUserChordInteractor;
        this.addUserChordInteractor.setListener(this);

        this.amazonS3Service = amazonS3Service;
        this.amazonS3Service.setImageListener(this);
        this.amazonS3Service.setUrlListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (LearnChordView) view;
        this.view.showProgressBar();
        this.view.enableLearntButton(!this.view.getLearntChord());

        amazonS3Service.getImage(this.view.getChord().getDiagramFilename());
    }

    @Override
    public void viewOnVideoRequested() {
        view.showProgressBar();
        amazonS3Service.getUrl(view.getChord().getVideoFilename());
    }

    @Override
    public void onImageDownloadFailed() {
        view.hideProgressBar();
        view.showImageLoadError();
    }

    @Override
    public void onImageDownloadSuccess(Bitmap bitmap) {
        view.hideProgressBar();
        view.setImage(bitmap);
    }

    @Override
    public void onUrlDownloadSuccess(String url) {
        view.hideProgressBar();
        view.playVideo(url);
    }

    @Override
    public void onUrlDownloadFailed() {
        view.hideProgressBar();
        view.showVideoLoadError();
    }

    @Override
    public void viewOnLearnt() {
        view.showLearntConfirmDialog();
    }

    @Override
    public void viewOnConfirmLearnt() {
        view.showProgressBar();
        addUserChordInteractor.addUserChord(loggedInUser.getApiKey(), loggedInUser.getUserId(),
                view.getChord().getId());
    }

    @Override
    public void onChordAdded(int level, int achievements) {
        view.hideProgressBar();
        if (level == 0 && achievements == 0) {
            view.showAddLearntChordSuccess();
        }
        else if (level != 0 && achievements != 0) {
            view.showAddLearntChordSuccess(level, achievements);
        }
        else {
            view.showAddLearntChordSuccess(achievements);
        }
    }

    @Override
    public void onAddChordError() {
        view.hideProgressBar();
        view.showAddLearntChordError();
    }

    @Override
    public void viewOnConfirmError() {
        view.finishActivity();
    }

    @Override
    public void viewOnConfirmLearntSuccess() {
        view.startLearnAllChordsActivity();
    }

    @Override
    public void viewOnHelpRequested() {
        view.startDiagramHelpActivity();
    }
}
