package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.listeners.AddUserChordListener;

/**
 * Interface for AddUserChordInteractor
 */
public interface IAddUserChordInteractor {
    void setListener(AddUserChordListener listener);
    void addUserChord(String apiKey, int userId, int chordId);
}
