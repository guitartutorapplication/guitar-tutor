package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.List;

/**
 * Interface for LearnAllChordsPresenter
 */
public interface ILearnViewAllChordsPresenter extends IPresenter {
    void modelOnChordsRetrieved(List<Chord> chords);
    void modelOnError();
}
