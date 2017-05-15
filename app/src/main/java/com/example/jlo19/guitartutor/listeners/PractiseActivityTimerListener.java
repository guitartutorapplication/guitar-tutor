package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.enums.PractiseActivityState;

/**
 *
 */
public interface PractiseActivityTimerListener {
    void onNewPractiseState(PractiseActivityState state, int chordIndex);
    void onPractiseActivityTimerError();
    void onFirstRoundOfChords();
}
