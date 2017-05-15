package com.example.jlo19.guitartutor.timers;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.listeners.PractiseActivityTimerListener;
import com.example.jlo19.guitartutor.timers.interfaces.IPractiseActivityTimer;

/**
 *
 */
public class PractiseActivityTimer implements IPractiseActivityTimer{

    private PractiseActivityTimerListener listener;
    private boolean requestStop;

    @Override
    public void setListener(PractiseActivityTimerListener listener) {
        this.listener = listener;
    }

    @Override
    public void start(final BeatSpeed beatSpeed, final ChordChange chordChange, final int numChords) {
        requestStop = false;

        final Runnable timerTask = new Runnable() {
            // round = one full run of all the chords
            int numRounds = 0;

            @Override
            public void run() {
                while (!requestStop) {
                    try {
                        for (int i = 0; i < numChords; i++) {
                            // inform listener that on the next chord in sequence
                            listener.onNewPractiseState(PractiseActivityState.NEW_CHORD, i);

                            // play sound for number of beats in chord change
                            for (int j = 0; j < chordChange.getValue(); j++) {
                                if (requestStop) {
                                    return;
                                }
                                // inform listener every beat
                                listener.onNewPractiseState(PractiseActivityState.NEW_BEAT, i);
                                Thread.sleep(beatSpeed.getValue());
                            }
                        }
                        numRounds++;
                        // after first round of chords
                        if (numRounds == 1) {
                            listener.onFirstRoundOfChords();
                        }
                    } catch (InterruptedException e) {
                        listener.onPractiseActivityTimerError();
                    }
                }
            }
        };

        Thread timer = new Thread(timerTask);
        timer.start();
    }

    @Override
    public void stop() {
        requestStop = true;
    }
}
