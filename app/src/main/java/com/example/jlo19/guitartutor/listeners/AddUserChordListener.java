package com.example.jlo19.guitartutor.listeners;

/**
 * Listener for AddUserChordInteractor
 */
public interface AddUserChordListener {
    void onChordAdded(int level, int achievements);
    void onAddChordError();
}
