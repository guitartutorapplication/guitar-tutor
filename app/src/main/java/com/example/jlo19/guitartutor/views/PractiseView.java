package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;

import java.util.List;

/**
 * View interface for PractiseActivity
 */
public interface PractiseView extends IView {
    void setToolbarTitleText();
    List<String> getSelectedChords();
    void setChordText(String chord);
    void showStopButton();
    void playMetronomeSound();
    void showError();
    void returnToPractiseSetup();
    void loadSounds();
    ChordChange getChordChange();
    BeatSpeed getBeatSpeed();
    void setCountdownText(String second);
    void hideCountdown();
    void hideFirstChordInstruction();
    void setFirstChordText(String firstChord);
    void playCountdownOneSound();
    void playCountdownTwoSound();
    void playCountdownThreeSound();
    void playCountdownGoSound();
}
