package com.example.jlo19.guitartutor.timers.interfaces;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.listeners.PractiseActivityTimerListener;

/**
 *
 */
public interface IPractiseActivityTimer {
    void setListener(PractiseActivityTimerListener listener);
    void start(BeatSpeed beatSpeed, ChordChange chordChange, int numChords);
    void stop();
}
