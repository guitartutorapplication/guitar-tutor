package com.example.jlo19.guitartutor.interactors.interfaces;

import com.example.jlo19.guitartutor.listeners.UpdateUserChordsListener;

import java.util.ArrayList;

/**
 * Interface for UpdateUserChordsInteractor
 */
public interface IUpdateUserChordsInteractor {
    void setListener(UpdateUserChordsListener listener);
    void updateUserChords(String apiKey, int userId, ArrayList<Integer> chordIds);
}