package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.Chord;

import java.util.List;

/**
 * View interface for AllChordActivity
 */
public interface AllChordsView {
    void setToolbarTitleText();
    void showProgressBar();
    void hideProgressBar();
    void setChords(List<Chord> chords);
    void showError();
}
