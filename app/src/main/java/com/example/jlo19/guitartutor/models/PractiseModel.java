package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.Countdown;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;

import java.util.List;

/**
 * Handles timer for practise activity
 */
public class PractiseModel implements IPractiseModel {

    private List<String> selectedChords;
    private boolean requestStop;
    private IPractisePresenter presenter;
    private Runnable timerTask;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;

    @Override
    public void setSelectedChords(List<String> selectedChords) {
        this.selectedChords = selectedChords;
    }

    @Override
    public void createPractiseTimer() {
        timerTask = new Runnable() {
            @Override
            public void run() {
                while (!requestStop) {
                    try {
                        for (int i = 0; i < selectedChords.size(); i++) {
                            // inform presenter that on the next chord in sequence
                            presenter.modelOnNewChord(selectedChords.get(i));

                            // play sound for number of beats in chord change
                            for (int j = 0; j < chordChange.getValue(); j++) {
                                if (requestStop) {
                                    return;
                                }
                                // inform presenter every beat
                                presenter.modelOnNewBeat();
                                Thread.sleep(beatSpeed.getValue());
                            }
                        }
                    }
                    catch (InterruptedException e) {
                        presenter.modelOnError();
                    }
                }
            }
        };
    }

    @Override
    public void setPresenter(IPractisePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startPractiseTimer() {
        requestStop = false;
        new Thread(timerTask).start();
    }

    @Override
    public void stopTimer() {
        requestStop = true;
    }

    @Override
    public void setChordChange(ChordChange chordChange) {
        this.chordChange = chordChange;
    }

    @Override
    public void setBeatSpeed(BeatSpeed beatSpeed) {
        this.beatSpeed = beatSpeed;
    }

    @Override
    public void startCountdown() {
        requestStop = false;
        Runnable countdown = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 4; i++) {
                        if (requestStop) {
                            return;
                        }

                        // inform presenter every second of countdown
                        presenter.modelOnNewSecondOfCountdown(Countdown.values()[i]);
                        if (i == 3) {
                            Thread.sleep(3000);
                        }
                        else {
                            Thread.sleep(1500);
                        }
                    }
                }
                catch (InterruptedException e) {
                    presenter.modelOnError();
                }
                presenter.modelOnCountdownFinished();
            }
        };

        new Thread(countdown).start();
    }
}
