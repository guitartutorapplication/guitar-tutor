package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.presenters.interfaces.ILearnViewAllChordsPresenter;

/**
 * Interface for LearnViewAllChordsModel
 */
public interface ILearnViewAllChordsModel {
    void setPresenter(ILearnViewAllChordsPresenter presenter);
    void getChords();
}
