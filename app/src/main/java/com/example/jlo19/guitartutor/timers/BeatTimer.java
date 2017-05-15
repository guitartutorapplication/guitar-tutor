package com.example.jlo19.guitartutor.timers;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.listeners.BeatTimerListener;
import com.example.jlo19.guitartutor.timers.interfaces.IBeatTimer;

/**
 * Timer to deal with standard counting of beat
 */
public class BeatTimer implements IBeatTimer {

    private BeatTimerListener listener;
    private boolean requestStop;
    private Thread timer;

    @Override
    public void start(final BeatSpeed beatSpeed) {
        requestStop = false;

        Runnable timerTask = new Runnable() {
            @Override
            public void run() {
                    try {
                        // timer only needed for 4 beats
                        for (int i = 0; i < 4; i++) {
                            if (requestStop) {
                                break;
                            }
                            // inform when new beat
                            listener.onNewBeat(i);
                            // thread sleeps for requested beat speed value
                            Thread.sleep(beatSpeed.getValue());
                        }
                        listener.onBeatTimerFinished();
                    } catch (InterruptedException e) {
                        listener.onBeatTimerError();
                    }
            }
        };

        timer = new Thread(timerTask);
        timer.start();
    }

    @Override
    public void stop() {
        // stop if there is currently a timer running
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
