package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.views.PractiseView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Testing PractisePresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class PractisePresenterTest {

    private IPractisePresenter presenter;
    private IPractiseModel model;
    private PractiseView view;
    private ArrayList<Chord> selectedChords;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;
    private SharedPreferences sharedPreferences;
    private List<String> audioFilenames;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new PractisePresenter();

        selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
            add(new Chord(4, "D", "MAJOR", "D.png", "D.mp4", "D.wav", 1));
        }};
        chordChange = ChordChange.EIGHT_BEATS;
        beatSpeed = BeatSpeed.FAST;
        view = Mockito.mock(PractiseView.class);
        Mockito.when(view.getSelectedChords()).thenReturn(selectedChords);
        Mockito.when(view.getChordChange()).thenReturn(chordChange);
        Mockito.when(view.getBeatSpeed()).thenReturn(beatSpeed);
        presenter.setView(view);

        sharedPreferences = Mockito.mock(SharedPreferences.class);
        presenter.setSharedPreferences(sharedPreferences);

        model = Mockito.mock(IPractiseModel.class);
        audioFilenames = Arrays.asList("countdown_3", "countdown_2", "countdown_1",
                "countdown_go", "metronome_sound", "A.wav", "B.wav", "C.wav", "D.wav");
        Mockito.when(model.getAudioFilenames()).thenReturn(audioFilenames);
        ((PractisePresenter) presenter).setModel(model);
    }

    @Test
    public void setModel_CallsLoadSoundOnView() {
        // assert
        Mockito.verify(view).loadSounds(audioFilenames);
    }

    @Test
    public void setView_CallsSetFirstChordTextOnViewWithFirstChord() {
        // assert
        Mockito.verify(view).setFirstChordText(selectedChords.get(0).toString());
    }

    @Test
    public void setModel_SetsSharedPreferencesOnModel() {
        // assert
        Mockito.verify(model).setSharedPreferences(sharedPreferences);
    }

    @Test
    public void setModel_CallsSetPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void setModel_CallsSetSelectedChordsOnModelWithViewChords() {
        // assert
        Mockito.verify(model).setSelectedChords(selectedChords);
    }

    @Test
    public void setModel_CallsSetChordChangeOnModelWithViewChordChange() {
        // assert
        Mockito.verify(model).setChordChange(chordChange);
    }

    @Test
    public void setModel_CallsSetBeatSpeedOnModelWithViewBeatSpeed() {
        // assert
        Mockito.verify(model).setBeatSpeed(beatSpeed);
    }

    @Test
    public void setModel_CallsCreatePractiseTimerOnModel() {
        // assert
        Mockito.verify(model).createPractiseTimer();
    }

    @Test
    public void modelOnNewChord_CallsSetChordTextOnViewWithChord() {
        // act
        int chordIndex = 1;
        presenter.modelOnNewPractiseState(PractiseActivityState.NEW_CHORD, chordIndex);

        // assert
        Mockito.verify(view).setChordText(selectedChords.get(chordIndex).toString());
    }

    @Test
    public void modelOnNewChord_CallPlaySoundOnView() {
        // act
        int chordIndex = 1;
        presenter.modelOnNewPractiseState(PractiseActivityState.NEW_CHORD, chordIndex);

        // assert
        Mockito.verify(view).playSound(PractiseActivityState.NEW_CHORD.ordinal() + chordIndex);
    }

    @Test
    public void modelOnNewBeat_CallsPlaySoundOnView() {
        // act
        presenter.modelOnNewPractiseState(PractiseActivityState.NEW_BEAT, 0);

        // assert
        Mockito.verify(view).playSound(PractiseActivityState.NEW_BEAT.ordinal());
    }

    @Test
    public void modelOnError_CallsShowErrorOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void modelOnError_CallsModelOnStopTimerOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(model).stopTimer();
    }

    @Test
    public void modelOnCountdownStage3_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_3;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void modelOnCountdownStage2_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_2;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void modelOnCountdownStage1_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_1;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void modelOnCountdownStageGo_CallsSetCountdownText() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_GO;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).setCountdownText(state.toString());
    }

    @Test
    public void modelOnCountdownStage3_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_3;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void modelOnCountdownStage2_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_2;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void modelOnCountdownStage1_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_1;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void modelOnCountdownStageGo_CallsPlaySoundOnView() {
        // act
        PractiseActivityState state = PractiseActivityState.COUNTDOWN_STAGE_GO;
        presenter.modelOnNewPractiseState(state, 0);

        // assert
        Mockito.verify(view).playSound(state.ordinal());
    }

    @Test
    public void modelOnCountdownFinished_CallsStartPractiseTimerOnModel() {
        // act
        presenter.modelOnCountdownFinished();

        // assert
        Mockito.verify(model).startPractiseTimer();
    }

    @Test
    public void modelOnCountdownFinished_CallsHideCountdownOnView() {
        // act
        presenter.modelOnCountdownFinished();

        // assert
        Mockito.verify(view).hideCountdown();
    }

    @Test
    public void modelOnCountdownFinished_CallsHideFirstChordInstructionOnView() {
        // act
        presenter.modelOnCountdownFinished();

        // assert
        Mockito.verify(view).hideFirstChordInstruction();
    }

    @Test
    public void modelOnFirstRoundOfChords_ShowStopButtonOnView() {
        // act
        presenter.modelOnFirstRoundOfChords();

        // assert
        Mockito.verify(view).showStopButton();
    }

    @Test
    public void viewOnDestroy_StopsTimerOnModel() {
        // act
        presenter.viewOnDestroy();

        // assert
        Mockito.verify(model).stopTimer();
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
    public void viewOnStopPractising_CallsSavePractiseSessionOnModel() {
        // act
        presenter.viewOnStopPractising();

        // assert
        Mockito.verify(model).savePractiseSession();
    }

    @Test
    public void modelOnPractiseSessionSaved_CallsStopTimerOnModel() {
        // act
        presenter.modelOnPractiseSessionSaved(0, 0);

        // assert
        Mockito.verify(model).stopTimer();
    }

    @Test
    public void modelOnPractiseSessionError_CallsStopTimerOnModel() {
        // act
        presenter.modelOnPractiseSessionSaveError();

        // assert
        Mockito.verify(model).stopTimer();
    }

    @Test
    public void modelOnPractiseSessionSavedWithZeroLevelAndAchievements_CallsShowPractiseSessionSaveSuccessOnView() {
        // act
        presenter.modelOnPractiseSessionSaved(0, 0);

        // assert
        Mockito.verify(view).showPractiseSessionSaveSuccess();
    }

    @Test
    public void modelOnPractiseSessionSavedWithNonZeroAchievements_CallsShowPractiseSessionSaveSuccessWithAchievementsOnView() {
        // act
        int achievements = 2800;
        presenter.modelOnPractiseSessionSaved(0, achievements);

        // assert
        Mockito.verify(view).showPractiseSessionSaveSuccess(achievements);
    }

    @Test
    public void modelOnPractiseSessionSavedWithNonZeroLevelAndAchievements_CallsShowPractiseSessionSaveWithAchievementsAndLevelOnView() {
        // act
        int achievements = 2000;
        int level = 3;
        presenter.modelOnPractiseSessionSaved(level, achievements);

        // assert
        Mockito.verify(view).showPractiseSessionSaveSuccess(level, achievements);
    }

    @Test
    public void modelOnPractiseSessionSaveError_CallsShowPractiseSessionSaveErrorOnView() {
        // act
        presenter.modelOnPractiseSessionSaveError();

        // assert
        Mockito.verify(view).showPractiseSessionSaveError();
    }

    @Test
    public void viewOnSoundLoadedForEachFilenameSuccessfully_CallsStartCountdownOnModel() {
        // act
        for (String ignored : audioFilenames) {
            presenter.viewOnSoundLoaded(0);
        }

        // assert
        Mockito.verify(model).startCountdown();
    }

    @Test
    public void viewOnSoundLoadedForEachFilenameWithOneUnsuccessful_CallsErrorOnView() {
        // act
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
