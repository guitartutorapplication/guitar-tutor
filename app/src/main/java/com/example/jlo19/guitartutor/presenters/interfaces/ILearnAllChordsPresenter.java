package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.GetChordsListener;

/**
 * Interface for LearnAllChordsPresenter
 */
public interface ILearnAllChordsPresenter extends IPresenter, GetChordsListener {
    void viewOnChordRequested(int chordPos);
    void viewOnConfirmError();
}
