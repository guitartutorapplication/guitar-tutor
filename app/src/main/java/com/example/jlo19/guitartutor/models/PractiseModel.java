package com.example.jlo19.guitartutor.models;

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

    @Override
    public void setSelectedChords(List<String> selectedChords) {
        this.selectedChords = selectedChords;
    }

    @Override
    public void createTimer() {
        timerTask = new Runnable() {
            @Override
            public void run() {
                while (!requestStop) {
                    try {
                        for (int i = 0; i < selectedChords.size(); i++) {
                            // inform presenter that on the next chord in sequence
                            presenter.modelOnNewChord(selectedChords.get(i));

                            // play sound for 8 seconds
                            for (int j = 0; j < 8; j++) {
                                if (requestStop) {
                                    return;
                                }
                                // inform presenter every second
                                presenter.modelOnNewSecond();
                                Thread.sleep(1000);
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
    public void start() {
        requestStop = false;
        new Thread(timerTask).start();
    }

    @Override
    public void stop() {
        requestStop = true;
    }
}
