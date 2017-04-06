package com.example.jlo19.guitartutor.models.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;

import java.util.List;

/**
 * Interface for LearnAllChordsModel
 */
public interface ILearnAllChordsModel {
    void setPresenter(ILearnAllChordsPresenter presenter);
    void getChords();
    void setSharedPreferences(SharedPreferences sharedPreferences);
    List<Chord> getAllChords();
    int getUserLevel();
    List<Integer> getUserChords();
}
