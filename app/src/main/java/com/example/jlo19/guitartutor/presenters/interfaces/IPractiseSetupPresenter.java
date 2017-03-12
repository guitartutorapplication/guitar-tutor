package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for PractiseSetupPresenter
 */
public interface IPractiseSetupPresenter extends IPresenter {
    void modelOnChordsRetrieved(List<Chord> chords);
    void modelOnLoadChordsError();
    void viewOnChordsSelected(ArrayList<String> selectedChords);
    void modelOnLessThanTwoChordsSelected();
    void modelOnSameSelectedChord();
    void modelOnCorrectSelectedChords(ArrayList<String> selectedChords);
}
