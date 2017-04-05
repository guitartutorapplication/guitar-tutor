package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.enums.Countdown;
import com.example.jlo19.guitartutor.models.retrofit.Chord;

/**
 * Interface for PractisePresenter
 */
public interface IPractisePresenter extends IPresenter {
    void modelOnNewChord(Chord newChord);
    void viewOnStopPractising();
    void modelOnNewBeat();
    void modelOnError();
    void modelOnNewSecondOfCountdown(Countdown countdownStage);
    void modelOnCountdownFinished();
    void viewOnSoundsLoaded();
    void viewOnDestroy();
    void viewOnStop();
    void viewOnPause();
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void modelOnPractiseSessionSaved(boolean result, int achievements);
    void modelOnFirstRoundOfChords();
}
