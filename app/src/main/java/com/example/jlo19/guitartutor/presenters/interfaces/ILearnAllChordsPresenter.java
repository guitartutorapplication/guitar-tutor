package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

/**
 * Interface for LearnAllChordsPresenter
 */
public interface ILearnAllChordsPresenter extends IPresenter {
    void modelOnChordsAndDetailsRetrieved();
    void modelOnError();
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void viewOnChordRequested(int chordPos);
    void viewOnConfirmError();
}
