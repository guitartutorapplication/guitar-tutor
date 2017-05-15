package com.example.jlo19.guitartutor.timers.interfaces;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.listeners.BeatTimerListener;

/**
 * Interface for BeatTimer
 */
public interface IBeatTimer {
    void start(BeatSpeed beatSpeed);
    void stop();
    void setListener(BeatTimerListener listener);
}
