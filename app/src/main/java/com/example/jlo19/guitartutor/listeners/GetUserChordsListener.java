package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.Chord;

import java.util.List;

/**
 * Listener for GetUserChordInteractor
 */
public interface GetUserChordsListener {
    void onUserChordsRetrieved(List<Chord> chords);
    void onGetUserChordsError();
}
