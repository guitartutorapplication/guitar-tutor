package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.Countdown;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
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

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new PractisePresenter();

        selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
            add(new Chord(4, "D", "MAJOR", "D.png", "D.mp4"));
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
        ((PractisePresenter) presenter).setModel(model);
    }

    @Test
    public void setModel_CallsLoadSoundOnView() {
        // assert
        Mockito.verify(view).loadSounds();
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
        presenter.modelOnNewChord(selectedChords.get(1));

        // assert
        Mockito.verify(view).setChordText(selectedChords.get(1).toString());
    }

    @Test
    public void modelOnNewBeat_CallsPlayMetronomeSoundOnView() {
        // act
        presenter.modelOnNewBeat();

        // assert
        Mockito.verify(view).playMetronomeSound();
    }

    @Test
    public void modelOnError_CallsShowErrorOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void modelOnError_CallsReturnToPractiseSetup() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).returnToPractiseSetup();
    }

    @Test
    public void modelOnNewSecondOfCountdown_CallsSetCountdownText() {
        // act
        presenter.modelOnNewSecondOfCountdown(Countdown.THREE);

        // assert
        Mockito.verify(view).setCountdownText(Countdown.THREE.toString());
    }

    @Test
    public void modelOnNewSecondOfCountdownWith3_CallsPlayCountdownThreeSoundOnView() {
        // act
        presenter.modelOnNewSecondOfCountdown(Countdown.THREE);

        // assert
        Mockito.verify(view).playCountdownThreeSound();
    }

    @Test
    public void modelOnNewSecondOfCountdownWith2_CallsPlayCountdownTwoSoundOnView() {
        // act
        presenter.modelOnNewSecondOfCountdown(Countdown.TWO);

        // assert
        Mockito.verify(view).playCountdownTwoSound();
    }

    @Test
    public void modelOnNewSecondOfCountdownWith1_CallsPlayCountdownOneSoundOnView() {
        // act
        presenter.modelOnNewSecondOfCountdown(Countdown.ONE);

        // assert
        Mockito.verify(view).playCountdownOneSound();
    }

    @Test
    public void modelOnNewSecondOfCountdownWithGo_CallsPlayCountdownGoSoundOnView() {
        // act
        presenter.modelOnNewSecondOfCountdown(Countdown.GO);

        // assert
        Mockito.verify(view).playCountdownGoSound();
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
    public void viewOnSoundsLoaded_StartCountdownOnModel() {
        // act
        presenter.viewOnSoundsLoaded();

        // assert
        Mockito.verify(model).startCountdown();
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
    public void modelOnPractiseSessionSavedWithSuccess_CallsShowPractiseSessionSaveSuccessOnView() {
        // act
        int achievements = 100;
        presenter.modelOnPractiseSessionSaved(true, achievements);

        // assert
        Mockito.verify(view).showPractiseSessionSaveSuccess(achievements);
    }

    @Test
    public void modelOnPractiseSessionSavedWithError_CallsShowPractiseSessionSaveErrorOnView() {
        // act
        presenter.modelOnPractiseSessionSaved(false, 0);

        // assert
        Mockito.verify(view).showPractiseSessionSaveError();
    }

    @Test
    public void modelOnPractiseSessionSaved_CallsReturnToPractiseSetupOnView() {
        // act
        presenter.modelOnPractiseSessionSaved(true, 0);

        // assert
        Mockito.verify(view).returnToPractiseSetup();
    }
}
