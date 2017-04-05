package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.Countdown;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.PractiseView;

import javax.inject.Inject;

/**
 * Presenter which provides the PractiseActivity with timer capabilities
 */
public class PractisePresenter implements IPractisePresenter {

    private PractiseView view;
    private IPractiseModel model;
    private SharedPreferences sharedPreferences;

    @Override
    public void setView(IView view) {
        this.view = (PractiseView) view;
        this.view.setFirstChordText(this.view.getSelectedChords().get(0).toString());

        App.getComponent().inject(this);
    }

    @Inject
    void setModel(IPractiseModel model) {
        this.model = model;
        model.setPresenter(this);
        model.setSelectedChords(view.getSelectedChords());
        model.setSharedPreferences(sharedPreferences);
        model.setChordChange(view.getChordChange());
        model.setBeatSpeed(view.getBeatSpeed());
        model.createPractiseTimer();

        view.loadSounds();
    }

    @Override
    public void modelOnNewChord(Chord chord) {
        view.setChordText(chord.toString());
    }

    @Override
    public void viewOnStopPractising() {
        model.savePractiseSession();
    }

    @Override
    public void modelOnNewBeat() {
        view.playMetronomeSound();
    }

    @Override
    public void modelOnError() {
        view.showError();
        view.returnToPractiseSetup();
    }

    @Override
    public void modelOnNewSecondOfCountdown(Countdown countdownStage) {
        view.setCountdownText(countdownStage.toString());

        switch (countdownStage) {
            case ONE:
                view.playCountdownOneSound();
                break;
            case TWO:
                view.playCountdownTwoSound();
                break;
            case THREE:
                view.playCountdownThreeSound();
                break;
            case GO:
                view.playCountdownGoSound();
                break;
        }
    }

    @Override
    public void modelOnCountdownFinished() {
        model.startPractiseTimer();
        view.hideCountdown();
        view.hideFirstChordInstruction();
    }

    @Override
    public void viewOnSoundsLoaded() {
        model.startCountdown();
    }

    @Override
    public void viewOnDestroy() {
        model.stopTimer();
    }

    @Override
    public void viewOnStop() {
        view.returnToPractiseSetup();
    }

    @Override
    public void viewOnPause() {
        view.returnToPractiseSetup();
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void modelOnPractiseSessionSaved(boolean result, int achievements) {
        if (result) {
            view.showPractiseSessionSaveSuccess(achievements);
        }
        else {
            view.showPractiseSessionSaveError();
        }

        view.returnToPractiseSetup();
    }

    @Override
    public void modelOnFirstRoundOfChords() {
        // stop button is only enabled once the user has completed one round of chords
        view.showStopButton();
    }
}
