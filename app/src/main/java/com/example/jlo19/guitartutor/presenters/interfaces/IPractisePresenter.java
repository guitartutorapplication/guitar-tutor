package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.enums.Countdown;

/**
 * Interface for PractisePresenter
 */
public interface IPractisePresenter extends IPresenter {
    void modelOnNewChord(String newChord);
    void viewOnStopPractising();
    void modelOnNewBeat();
    void modelOnError();
    void modelOnNewSecondOfCountdown(Countdown countdownStage);
    void modelOnCountdownFinished();
    void viewOnSoundsLoaded();
    void viewOnDestroy();
    void viewOnStop();
}
