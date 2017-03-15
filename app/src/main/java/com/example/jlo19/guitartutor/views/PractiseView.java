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
    void setStopButtonVisibility(int isVisible);
    void setStartButtonVisibility(int isVisible);
    void playSound();
    void showError();
    void startPractiseSetupActivity();
    void loadSound();
    ChordChange getChordChange();
    BeatSpeed getBeatSpeed();
}
