package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.BeatTimerListener;
import com.example.jlo19.guitartutor.listeners.PractiseActivityTimerListener;
import com.example.jlo19.guitartutor.listeners.UpdateUserChordsListener;

/**
 * Interface for PractisePresenter
 */
public interface IPractisePresenter extends IPresenter, UpdateUserChordsListener, BeatTimerListener,
        PractiseActivityTimerListener{
    void viewOnStopPractising();
    void viewOnSoundLoaded(int status);
    void viewOnDestroy();
    void viewOnStop();
    void viewOnPause();
    void viewOnConfirmSuccess();
    void viewOnConfirmError();
}
