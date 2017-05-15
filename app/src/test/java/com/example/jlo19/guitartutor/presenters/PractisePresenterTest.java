package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.interactors.interfaces.IUpdateUserChordsInteractor;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.timers.interfaces.IBeatTimer;
import com.example.jlo19.guitartutor.timers.interfaces.IPractiseActivityTimer;
import com.example.jlo19.guitartutor.views.PractiseView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

/**
 * Testing PractisePresenter
 */
public class PractisePresenterTest {

    private IPractisePresenter presenter;
    private IUpdateUserChordsInteractor updateUserChordsInteractor;
    private PractiseView view;
    private ArrayList<Chord> selectedChords;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;
    private LoggedInUser loggedInUser;
    private IBeatTimer beatTimer;
    private IPractiseActivityTimer practiseActivityTimer;
    private ArrayList<String> audioFilenames;

    @Before
    public void setUp() {
        updateUserChordsInteractor = Mockito.mock(IUpdateUserChordsInteractor.class);
        loggedInUser = Mockito.mock(LoggedInUser.class);
        beatTimer = Mockito.mock(IBeatTimer.class);
        practiseActivityTimer = Mockito.mock(IPractiseActivityTimer.class);
        presenter = new PractisePresenter(updateUserChordsInteractor, loggedInUser, beatTimer,
                practiseActivityTimer);

        selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
            add(new Chord(4, "D", "MAJOR", "D.png", "D.mp4", "D.wav", 1));
        }};
        chordChange = ChordChange.EIGHT_BEATS;
        beatSpeed = BeatSpeed.FAST;

        // audio filenames includes filenames from all states (e.g. new beat, countdown stage 1)
        // and filenames from chords
        audioFilenames = new ArrayList<>();
        for (PractiseActivityState state : PractiseActivityState.values()) {
            if (state == PractiseActivityState.NEW_CHORD) {
                for (Chord chord : selectedChords) {
                    audioFilenames.add(chord.getAudioFilename());
                }
            } else {
                audioFilenames.add(state.getFilename());
            }
        }

        view = Mockito.mock(PractiseView.class);
        Mockito.when(view.getSelectedChords()).thenReturn(selectedChords);
        Mockito.when(view.getChordChange()).thenReturn(chordChange);
        Mockito.when(view.getBeatSpeed()).thenReturn(beatSpeed);
        presenter.setView(view);
    }

    @Test
    public void setView_CallsLoadSoundOnView() {
        // assert
        Mockito.verify(view).loadSounds(audioFilenames);
    }

    @Test
    public void setView_CallsSetFirstChordTextOnViewWithFirstChord() {
        // assert
        Mockito.verify(view).setFirstChordText(selectedChords.get(0).toString());
    }

    @Test
    public void callsSetListenerOnInteractor() {
        // assert
        Mockito.verify(updateUserChordsInteractor).setListener(presenter);
    }

    @Test
    public void onNewChord_CallsSetChordTextOnViewWithChord() {
        // act
        int chordIndex = 1;
        presenter.onNewPractiseState(PractiseActivityState.NEW_CHORD, chordIndex);

        // assert
        Mockito.verify(view).setChordText(selectedChords.get(chordIndex).toString());
    }

    @Test
    public void onNewChord_CallPlaySoundOnView() {
        // act
        int chordIndex = 1;
        presenter.onNewPractiseState(PractiseActivityState.NEW_CHORD, chordIndex);

        // assert
        Mockito.verify(view).playSound(PractiseActivityState.NEW_CHORD.ordinal() + chordIndex);
    }

    @Test
    public void onNewBeatInPractiseActivity_CallsPlaySoundOnView() {
        // act
        presenter.onNewPractiseState(PractiseActivityState.NEW_BEAT, 0);

        // assert
        Mockito.verify(view).playSound(PractiseActivityState.NEW_BEAT.ordinal());
    }

    @Test
    public void onPractiseActivityTimerError_CallsShowErrorOnView() {
        // act
        presenter.onPractiseActivityTimerError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void onBeatTimerError_CallsShowErrorOnView() {
        // act
        presenter.onBeatTimerError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void onCountdownStage3_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_3;
        presenter.onNewBeat(0);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void onCountdownStage2_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_2;
        presenter.onNewBeat(1);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void onCountdownStage1_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_1;
        presenter.onNewBeat(2);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void onCountdownStageGo_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_GO;
        presenter.onNewBeat(3);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void onCountdownStage3_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_3;
        presenter.onNewBeat(0);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void onCountdownStage2_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_2;
        presenter.onNewBeat(1);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void onCountdownStage1_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_1;
        presenter.onNewBeat(2);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void onCountdownStageGo_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_GO;
        presenter.onNewBeat(3);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void onCountdownFinished_CallsStartOnPractiseActivityTimer() {
        // act
        presenter.onBeatTimerFinished();

        // assert
        Mockito.verify(practiseActivityTimer).start(beatSpeed, chordChange, selectedChords.size());
    }

    @Test
    public void onCountdownFinished_CallsHideCountdownOnView() {
        // act
        presenter.onBeatTimerFinished();

        // assert
        Mockito.verify(view).hideCountdown();
    }

    @Test
    public void onCountdownFinished_CallsHideFirstChordInstructionOnView() {
        // act
        presenter.onBeatTimerFinished();

        // assert
        Mockito.verify(view).hideFirstChordInstruction();
    }

    @Test
    public void onFirstRoundOfChords_ShowStopButtonOnView() {
        // act
        presenter.onFirstRoundOfChords();

        // assert
        Mockito.verify(view).showStopButton();
    }

    @Test
    public void viewOnDestroy_CallsStopOnTimers() {
        // act
        presenter.viewOnDestroy();

        // assert
        Mockito.verify(beatTimer).stop();
        Mockito.verify(practiseActivityTimer).stop();
    }

    @Test
    public void viewOnStop_CallsReturnToPractiseSetupOnView() {
        // act
        presenter.viewOnStop();

        // assert
        Mockito.verify(view).returnToPractiseSetup();
    }

    @Test
    public void viewOnPause_CallsReturnToPractiseSetupOnView() {
        // act
        presenter.viewOnPause();

        // assert
        Mockito.verify(view).returnToPractiseSetup();
    }

    @Test
    public void viewOnStopPractising_CallsUpdateUserChordsOnInteractor() {
        // act
        presenter.viewOnStopPractising();

        // assert
        // gets list of only chord ids
        ArrayList<Integer> chordIds = new ArrayList<>();
        for (Chord chord : view.getSelectedChords()){
            chordIds.add(chord.getId());
        }
        Mockito.verify(updateUserChordsInteractor).updateUserChords(loggedInUser.getApiKey(),
                loggedInUser.getUserId(), chordIds);
    }

    @Test
    public void viewOnStopPractising_CallsStopOnPractiseActivityTimer() {
        // act
        presenter.viewOnStopPractising();

        // assert
        Mockito.verify(practiseActivityTimer).stop();
    }

    @Test
    public void onUpdateUserChordsSuccessWithZeroLevelAndAchievements_CallsShowPractiseSessionSaveSuccessOnView() {
        // act
        presenter.onUpdateUserChordsSuccess(0, 0);

        // assert
        Mockito.verify(view).showPractiseSessionSaveSuccess();
    }

    @Test
    public void onUpdateUserChordsSuccessWithNonZeroAchievements_CallsShowPractiseSessionSaveSuccessWithAchievementsOnView() {
        // act
        int achievements = 2800;
        presenter.onUpdateUserChordsSuccess(0, achievements);

        // assert
        Mockito.verify(view).showPractiseSessionSaveSuccess(achievements);
    }

    @Test
    public void onUpdateUserChordsSuccessWithNonZeroLevelAndAchievements_CallsShowPractiseSessionSaveWithAchievementsAndLevelOnView() {
        // act
        int achievements = 2000;
        int level = 3;
        presenter.onUpdateUserChordsSuccess(level, achievements);

        // assert
        Mockito.verify(view).showPractiseSessionSaveSuccess(level, achievements);
    }

    @Test
    public void onUpdateUserChordsError_CallsShowPractiseSessionSaveErrorOnView() {
        // act
        presenter.onUpdateUserChordsError();

        // assert
        Mockito.verify(view).showPractiseSessionSaveError();
    }

    @Test
    public void viewOnSoundLoadedForEachFilenameSuccessfully_CallsStartOnBeatTimer() {
        // act
        for (String ignored : audioFilenames) {
            presenter.viewOnSoundLoaded(0);
        }

        // assert
        Mockito.verify(beatTimer).start(BeatSpeed.VERY_SLOW);
    }

    @Test
    public void viewOnSoundLoadedForEachFilenameWithOneUnsuccessful_CallsErrorOnView() {
        // act
        // 0 = success and 1 = error
        for (int i = 0; i < audioFilenames.size()-1; i++) {
            presenter.viewOnSoundLoaded(0);
        }
        presenter.viewOnSoundLoaded(1);

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void viewOnConfirmSuccess_CallsReturnToPractiseSetupOnView() {
        // act
        presenter.viewOnConfirmSuccess();

        // assert
        Mockito.verify(view).returnToPractiseSetup();
    }

    @Test
    public void viewOnConfirmError_CallsReturnToPractiseSetupOnView() {
        // act
        presenter.viewOnConfirmError();

        // assert
        Mockito.verify(view).returnToPractiseSetup();
    }
}
