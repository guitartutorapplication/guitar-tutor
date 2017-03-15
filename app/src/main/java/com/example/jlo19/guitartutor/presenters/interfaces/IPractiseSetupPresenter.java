package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for PractiseSetupPresenter
 */
public interface IPractiseSetupPresenter extends IPresenter {
    void modelOnChordsRetrieved(List<Chord> chords);
    void modelOnLoadChordsError();
    void viewOnPractise(ArrayList<String> selectedChords, int chordChangeSpeedIndex);
    void modelOnLessThanTwoChordsSelected();
    void modelOnSameSelectedChord();
    void modelOnCorrectSelectedChords(ArrayList<String> selectedChords, ChordChange chordChange);
}
