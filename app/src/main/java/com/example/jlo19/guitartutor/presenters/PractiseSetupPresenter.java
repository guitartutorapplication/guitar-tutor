package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.timers.interfaces.IBeatTimer;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.PractiseSetupView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Presenter that provides PractiseSetupActivity with DB API interaction and timer capabilities
 */
public class PractiseSetupPresenter implements IPractiseSetupPresenter {

    private final IGetUserChordsInteractor getUserChordsInteractor;
    private final LoggedInUser loggedInUser;
    private final IBeatTimer beatTimer;
    private PractiseSetupView view;

    public PractiseSetupPresenter(IGetUserChordsInteractor getUserChordsInteractor, LoggedInUser loggedInUser,
                                  IBeatTimer beatTimer) {
        this.loggedInUser = loggedInUser;
        this.beatTimer = beatTimer;
        this.beatTimer.setListener(this);
        this.getUserChordsInteractor = getUserChordsInteractor;
        this.getUserChordsInteractor.setListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (PractiseSetupView) view;
        this.view.showProgressBar();
        this.view.loadSound();

        // gets user chords from DB
        getUserChordsInteractor.getUserChords(loggedInUser.getApiKey(), loggedInUser.getUserId());
    }

    @Override
    public void viewOnPractise(List<Chord> selectedChords, int chordChangeIndex, int beatSpeedIndex) {
        // removing any null values (left on default option)
        List<Chord> chosenChords = new ArrayList<>();
        for (Chord chord : selectedChords) {
            if (chord != null) {
                chosenChords.add(chord);
            }
        }

        Set<Chord> uniqueChords = new HashSet<>(chosenChords);

        // if the user has selected less than two chords
        if (chosenChords.size() < 2) {
            view.showLessThanTwoChordsSelectedError();
        }
        // if the user has selected the same chord more than once
        else if (uniqueChords.size() < chosenChords.size()) {
            view.showSameSelectedChordError();
        }
        else {
            view.startPractiseActivity(chosenChords, ChordChange.values()[chordChangeIndex],
                    BeatSpeed.values()[beatSpeedIndex]);
        }
    }

    @Override
    public void viewOnBeatPreview(int beatSpeedIndex) {
        view.enablePreviewButton(false);
        beatTimer.start(BeatSpeed.values()[beatSpeedIndex]);
    }

    @Override
    public void viewOnBeatSpeedChanged() {
        view.enablePreviewButton(true);
        // stop playing preview
        beatTimer.stop();
    }

    @Override
    public void viewOnDestroy() {
        beatTimer.stop();
    }

    @Override
    public void viewOnPause() {
        view.enablePreviewButton(true);
        beatTimer.stop();
    }

    @Override
    public void viewOnStop() {
        view.enablePreviewButton(true);
        beatTimer.stop();
    }

    @Override
    public void viewOnConfirmError() {
        view.finishActivity();
    }

    @Override
    public void onUserChordsRetrieved(List<Chord> chords) {
        view.hideProgressBar();
        view.setChords(chords);
    }

    @Override
    public void onGetUserChordsError() {
        view.hideProgressBar();
        view.showLoadChordsError();
    }

    @Override
    public void onNewBeat(int numOfBeats) {
        // once preview has been playing for long enough, stop timer
        if (numOfBeats > 4) {
            view.enablePreviewButton(true);
            beatTimer.stop();
        }
        else {
            view.playSound();
        }
    }

    @Override
    public void onBeatTimerError() {
        view.showPreviewBeatError();
    }
}
