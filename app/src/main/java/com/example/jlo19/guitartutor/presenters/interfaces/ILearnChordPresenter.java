package com.example.jlo19.guitartutor.presenters.interfaces;

import android.graphics.Bitmap;

/**
 * Interface for LearnChordPresenter
 */
public interface ILearnChordPresenter extends IPresenter {
    void viewOnVideoRequested();
    void modelOnImageDownloadFailed();
    void modelOnImageDownloadSuccess(Bitmap bitmap);
    void modelOnVideoDownloadSuccess(String url);
    void modelOnVideoDownloadFailed();
}
