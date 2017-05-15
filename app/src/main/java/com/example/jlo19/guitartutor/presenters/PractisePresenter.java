package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.models.interfaces.IUpdateUserChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.timers.interfaces.IBeatTimer;
import com.example.jlo19.guitartutor.timers.interfaces.IPractiseActivityTimer;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.PractiseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter which provides the PractiseActivity with timer capabilities
 */
public class PractisePresenter implements IPractisePresenter {

    private final LoggedInUser loggedInUser;
    private IBeatTimer beatTimer;
    private IPractiseActivityTimer practiseActivityTimer;
    private PractiseView view;
    private IUpdateUserChordsInteractor updateUserChordsInteractor;
    private int numSoundsLoaded;
    private int numSoundsLoadedSuccess;

    public PractisePresenter(IUpdateUserChordsInteractor updateUserChordsInteractor, LoggedInUser loggedInUser,
                             IBeatTimer beatTimer, IPractiseActivityTimer practiseActivityTimer) {
        this.loggedInUser = loggedInUser;

        this.beatTimer = beatTimer;
        this.beatTimer.setListener(this);

        this.practiseActivityTimer = practiseActivityTimer;
        this.practiseActivityTimer.setListener(this);

        this.updateUserChordsInteractor = updateUserChordsInteractor;
        this.updateUserChordsInteractor.setListener(this);

        numSoundsLoaded = 0;
        numSoundsLoadedSuccess = 0;
    }

    @Override
    public void setView(IView view) {
        this.view = (PractiseView) view;
        this.view.setFirstChordText(this.view.getSelectedChords().get(0).toString());
        this.view.loadSounds(getAudioFilenames());
    }

    private List<String> getAudioFilenames() {
        List<String> filenames = new ArrayList<>();
        for (PractiseActivityState state : PractiseActivityState.values()) {
            if (state == PractiseActivityState.NEW_CHORD) {
                for (Chord chord : view.getSelectedChords()) {
                    filenames.add(chord.getAudioFilename());
                }
            } else {
                filenames.add(state.getFilename());
            }
        }
        return filenames;
    }

    @Override
    public void viewOnStopPractising() {
        practiseActivityTimer.stop();

        ArrayList<Integer> chordIds = new ArrayList<>();
        for (Chord chord : view.getSelectedChords()){
            chordIds.add(chord.getId());
        }
        updateUserChordsInteractor.updateUserChords(loggedInUser.getApiKey(), loggedInUser.getUserId(),
                chordIds);
    }

    @Override
    public void viewOnSoundLoaded(int status) {
        numSoundsLoaded++;
        // this means load was a success
        if (status == 0) {
            numSoundsLoadedSuccess++;
        }

        if (numSoundsLoaded == getAudioFilenames().size()) {
            // start countdown only if all sounds are successfully loaded
            if (numSoundsLoaded == numSoundsLoadedSuccess) {
                beatTimer.start(BeatSpeed.VERY_SLOW);
            }
            // show error is not all sounds were successfully loaded
            else {
                view.showError();
            }
        }
    }

    @Override
    public void viewOnDestroy() {
        beatTimer.stop();
        practiseActivityTimer.stop();
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
    public void onUpdateUserChordsSuccess(int level, int achievements) {
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
    public void onUpdateUserChordsError() {
        view.showPractiseSessionSaveError();
    }

    @Override
    public void viewOnConfirmSuccess() {
        view.returnToPractiseSetup();
    }

    @Override
    public void viewOnConfirmError() {
        view.returnToPractiseSetup();
    }

    private void onCountdownFinished() {
        beatTimer.stop();
        practiseActivityTimer.start(view.getBeatSpeed(), view.getChordChange(),
                view.getSelectedChords().size());

        view.hideCountdown();
        view.hideFirstChordInstruction();
    }

    @Override
    public void onNewBeat(int numOfBeats) {
        if (numOfBeats <= 4) {
            int index = numOfBeats - 1;
            view.playSound(index);
            view.setCountdownText(PractiseActivityState.values()[index].toString());
        }
        else {
            onCountdownFinished();
        }
    }

    @Override
    public void onBeatTimerError() {
        view.showError();
    }

    @Override
    public void onNewPractiseState(PractiseActivityState state, int chordIndex) {
        if (state == PractiseActivityState.NEW_BEAT) {
            view.playSound(state.ordinal());
        }
        else {
            view.playSound(state.ordinal() + chordIndex);
            // if in new chord state, set chord text with current chord name
            view.setChordText(view.getSelectedChords().get(chordIndex).toString());
        }
    }

    @Override
    public void onPractiseActivityTimerError() {
        view.showError();
    }

    @Override
    public void onFirstRoundOfChords() {
        view.showStopButton();
    }
}
