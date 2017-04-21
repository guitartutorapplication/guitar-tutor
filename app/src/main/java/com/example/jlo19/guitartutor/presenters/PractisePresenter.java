package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
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
    private int numSoundsLoaded;
    private int numSoundsLoadedSuccess;

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

        numSoundsLoaded = 0;
        numSoundsLoadedSuccess = 0;
        view.loadSounds(model.getAudioFilenames());
    }

    @Override
    public void viewOnStopPractising() {
        model.savePractiseSession();
    }

    @Override
    public void modelOnError() {
        view.showError();
        model.stopTimer();
    }

    @Override
    public void modelOnCountdownFinished() {
        model.startPractiseTimer();
        view.hideCountdown();
        view.hideFirstChordInstruction();
    }

    @Override
    public void viewOnSoundLoaded(int status) {
        numSoundsLoaded++;
        // this means load was a success
        if (status == 0) {
            numSoundsLoadedSuccess++;
        }

        if (numSoundsLoaded == model.getAudioFilenames().size()) {
            // start countdown only if all sounds are successfully loaded
            if (numSoundsLoaded == numSoundsLoadedSuccess) {
                model.startCountdown();
            }
            // show error is not all sounds were successfully loaded
            else {
                view.showError();
            }
        }
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
    public void modelOnPractiseSessionSaved(int level, int achievements) {
        model.stopTimer();
        if (level == 0 && achievements == 0) {
            view.showPractiseSessionSaveSuccess();
        }
        else if (level != 0 && achievements != 0) {
            view.showPractiseSessionSaveSuccess(level, achievements);
        }
        else {
            view.showPractiseSessionSaveSuccess(achievements);
        }
    }

    @Override
    public void modelOnFirstRoundOfChords() {
        // stop button is only enabled once the user has completed one round of chords
        view.showStopButton();
    }

    @Override
    public void modelOnPractiseSessionSaveError() {
        model.stopTimer();
        view.showPractiseSessionSaveError();
    }

    @Override
    public void modelOnNewPractiseState(PractiseActivityState state, int currentChordIndex) {
        if (state != PractiseActivityState.NEW_CHORD) {
            view.playSound(state.ordinal());
            // if in any of the countdown states, set the countdown text with message
            if (state == PractiseActivityState.COUNTDOWN_STAGE_1 || state ==
                    PractiseActivityState.COUNTDOWN_STAGE_2 || state == PractiseActivityState.
                    COUNTDOWN_STAGE_3  || state == PractiseActivityState.COUNTDOWN_STAGE_GO) {
                view.setCountdownText(state.toString());
            }
        }
        else {
            view.playSound(state.ordinal() + currentChordIndex);
            // if in new chord state, set chord text with current chord name
            view.setChordText(view.getSelectedChords().get(currentChordIndex).toString());
        }
    }

    @Override
    public void viewOnConfirmSuccess() {
        view.returnToPractiseSetup();
    }

    @Override
    public void viewOnConfirmError() {
        view.returnToPractiseSetup();
    }
}
