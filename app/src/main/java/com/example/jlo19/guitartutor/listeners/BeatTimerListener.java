package com.example.jlo19.guitartutor.listeners;

/**
 * Listener for BeatTimer
 */
public interface BeatTimerListener {
    void onNewBeat(int index);
    void onBeatTimerError();
    void onBeatTimerFinished();
}
