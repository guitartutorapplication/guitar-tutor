package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.List;

/**
 * Interface for LearnAllChordsPresenter
 */
public interface ILearnAllChordsPresenter extends IPresenter {
    void modelOnChordsRetrieved(List<Chord> chords, List<Integer> userChords);
    void modelOnError();
    void setSharedPreferences(SharedPreferences sharedPreferences);
}
