package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.ArrayList;
import java.util.List;

/**
 * View interface for PractiseSetupActivity
 */
public interface PractiseSetupView extends IView {
    void setToolbarTitleText();
    void showProgressBar();
    void hideProgressBar();
    void setChords(List<Chord> chords);
    void showLoadChordsError();
    void showLessThanTwoChordsSelectedError();
    void showSameSelectedChordError();
    void startPractiseActivity(ArrayList<String> selectedChords, ChordChange chordChange, BeatSpeed beatSpeed);
}
