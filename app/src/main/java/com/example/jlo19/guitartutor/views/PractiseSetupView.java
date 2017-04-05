package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.List;

/**
 * View interface for PractiseSetupActivity
 */
public interface PractiseSetupView extends IProgressBarView {
    void setChords(List<Chord> chords);
    void showLoadChordsError();
    void showLessThanTwoChordsSelectedError();
    void showSameSelectedChordError();
    void startPractiseActivity(List<Chord> selectedChords, ChordChange chordChange, BeatSpeed beatSpeed);
    void playSound();
    void loadSound();
    void showPreviewBeatError();
    void enablePreviewButton(boolean isEnabled);
}
