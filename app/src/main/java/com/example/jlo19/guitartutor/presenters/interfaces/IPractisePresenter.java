package com.example.jlo19.guitartutor.presenters.interfaces;

/**
 * Interface for PractisePresenter
 */
public interface IPractisePresenter extends IPresenter {
    void modelOnNewChord(String newChord);
    void viewOnStartPractising();
    void viewOnStopPractising();
    void modelOnNewBeat();
    void modelOnError();
}
