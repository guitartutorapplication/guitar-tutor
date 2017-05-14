package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.BeatTimerListener;
import com.example.jlo19.guitartutor.listeners.GetUserChordsListener;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

/**
 * Interface for PractiseSetupPresenter
 */
public interface IPractiseSetupPresenter extends IPresenter, GetUserChordsListener, BeatTimerListener {
    void viewOnPractise(List<Chord> selectedChords, int chordChangeIndex, int beatSpeedIndex);
    void viewOnBeatPreview(int beatSpeedIndex);
    void viewOnBeatSpeedChanged();
    void viewOnDestroy();
    void viewOnPause();
    void viewOnStop();
    void viewOnConfirmError();
}
