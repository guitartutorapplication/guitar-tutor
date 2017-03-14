package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.List;

/**
 * View interface for activities that get all chords from DB
 */
public interface LearnViewAllChordsView extends IView {
    void setToolbarTitleText();
    void showProgressBar();
    void hideProgressBar();
    void setChords(List<Chord> chords);
    void showError();
}