package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

/**
 * View interface for PractiseActivity
 */
public interface PractiseView extends IView {
    List<Chord> getSelectedChords();
    void setChordText(String chord);
    void showStopButton();
    void playSound(int index);
    void showError();
    void returnToPractiseSetup();
    void loadSounds(List<String> filenames);
    ChordChange getChordChange();
    BeatSpeed getBeatSpeed();
    void setCountdownText(String second);
    void hideCountdown();
    void hideFirstChordInstruction();
    void setFirstChordText(String firstChord);
    void showPractiseSessionSaveSuccess(int achievements);
    void showPractiseSessionSaveError();
    void showPractiseSessionSaveSuccess(int level, int achievements);
    void showPractiseSessionSaveSuccess();
}
