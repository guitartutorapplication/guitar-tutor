package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.enums.PractiseActivityState;

/**
 * Interface for PractisePresenter
 */
public interface IPractisePresenter extends IPresenter {
    void viewOnStopPractising();
    void modelOnError();
    void modelOnCountdownFinished();
    void viewOnSoundLoaded(int status);
    void viewOnDestroy();
    void viewOnStop();
    void viewOnPause();
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void modelOnPractiseSessionSaved(int level, int achievements);
    void modelOnFirstRoundOfChords();
    void modelOnPractiseSessionSaveError();
    void modelOnNewPractiseState(PractiseActivityState state, int currentChordIndex);
    void viewOnConfirmSuccess();
    void viewOnConfirmError();
}
