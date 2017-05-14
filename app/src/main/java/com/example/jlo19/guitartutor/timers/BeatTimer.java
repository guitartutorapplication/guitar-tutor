package com.example.jlo19.guitartutor.timers;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.listeners.BeatTimerListener;
import com.example.jlo19.guitartutor.timers.interfaces.IBeatTimer;

/**
 *
 */
public class BeatTimer implements IBeatTimer {

    private BeatTimerListener listener;
    private boolean requestStop;
    private Thread timer;
    private int numOfBeats;

    @Override
    public void start(final BeatSpeed beatSpeed) {
        requestStop = false;

        numOfBeats = 1;

        Runnable timerTask = new Runnable() {
            @Override
            public void run() {
                while (!requestStop) {
                    try {
                        // inform listener every beat
                        listener.onNewBeat(numOfBeats);
                        Thread.sleep(beatSpeed.getValue());
                        numOfBeats++;
                    } catch (InterruptedException e) {
                        listener.onTimerError();
                    }
                }
            }
        };

        timer = new Thread(timerTask);
        timer.start();
    }

    @Override
    public void stop() {
        // stop if there is currently a beat preview running
        if (timer != null) {
            if (timer.isAlive()) {
                requestStop = true;
            }
        }
    }

    @Override
    public void setListener(BeatTimerListener listener) {
        this.listener = listener;
    }
}
