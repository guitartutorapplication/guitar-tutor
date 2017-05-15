package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.Chord;

import java.util.List;

/**
 * Listener for GetChordsInteractor
 */
public interface GetChordsListener {
    void onChordsAndDetailsRetrieved(List<Chord> allChords, int userLevel, List<Integer> userChords);
    void onError();
}
