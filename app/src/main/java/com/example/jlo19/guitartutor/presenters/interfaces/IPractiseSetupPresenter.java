package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

/**
 * Interface for PractiseSetupPresenter
 */
public interface IPractiseSetupPresenter extends IPresenter {
    void modelOnChordsRetrieved(List<Chord> chords);
    void modelOnLoadChordsError();
    void viewOnPractise(List<Chord> selectedChords, int chordChangeIndex, int beatSpeedIndex);
    void modelOnLessThanTwoChordsSelected();
    void modelOnSameSelectedChord();
    void modelOnCorrectSelectedChords(List<Chord> selectedChords, ChordChange chordChange, BeatSpeed beatSpeed);
    void viewOnBeatPreview(int beatSpeedIndex);
    void modelOnNewBeat();
    void modelOnPreviewBeatError();
    void modelOnBeatPreviewFinished();
    void viewOnBeatSpeedChanged();
    void viewOnDestroy();
    void viewOnPause();
    void viewOnStop();
    void setSharedPreferences(SharedPreferences sharedPreferences);
}
