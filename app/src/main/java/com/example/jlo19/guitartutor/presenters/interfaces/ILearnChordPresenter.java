package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

/**
 * Interface for LearnChordPresenter
 */
public interface ILearnChordPresenter extends IPresenter {
    void viewOnVideoRequested();
    void modelOnImageDownloadFailed();
    void modelOnImageDownloadSuccess(Bitmap bitmap);
    void modelOnUrlDownloadSuccess(String url);
    void modelOnUrlDownloadFailed();
    void viewOnLearnt();
    void viewOnConfirmLearnt();
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void modelOnLearntChordAdded(int level, int achievements);
    void modelOnAddLearntChordError();
    void viewOnConfirmError();
    void viewOnConfirmLearntSuccess();
}
