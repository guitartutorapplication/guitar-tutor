package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;

import java.util.List;

/**
 * Interface for PractiseModel
 */
public interface IPractiseModel {
    void setSelectedChords(List<String> selectedChords);
    void createTimer();
    void setPresenter(IPractisePresenter presenter);
    void startTimer();
    void stopTimer();
    void setChordChange(ChordChange chordChange);
    void setBeatSpeed(BeatSpeed beatSpeed);
}
