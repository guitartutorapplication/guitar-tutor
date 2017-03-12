package com.example.jlo19.guitartutor.presenters.interfaces;

import android.graphics.Bitmap;

/**
 * Interface for LearnChordPresenter
 */
public interface ILearnChordPresenter extends IPresenter {
    void viewOnVideoRequested();
    void modelOnDownloadFailed();
    void modelOnDownloadSuccess(Bitmap bitmap);
    void modelOnDownloadSuccess(String url);
}
