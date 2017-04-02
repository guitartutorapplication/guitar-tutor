package com.example.jlo19.guitartutor.models.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;

/**
 * Interface for LearnAllChordsModel
 */
public interface ILearnAllChordsModel {
    void setPresenter(ILearnAllChordsPresenter presenter);
    void getChords();
    void setSharedPreferences(SharedPreferences sharedPreferences);
}
