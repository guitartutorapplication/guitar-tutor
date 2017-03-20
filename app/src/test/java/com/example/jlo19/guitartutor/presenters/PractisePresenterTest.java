package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.Countdown;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
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
    private ArrayList<String> selectedChords;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new PractisePresenter();

        selectedChords = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
            add("D");
        }};
        chordChange = ChordChange.EIGHT_BEATS;
        beatSpeed = BeatSpeed.FAST;
        view = Mockito.mock(PractiseView.class);
        Mockito.when(view.getSelectedChords()).thenReturn(selectedChords);
        Mockito.when(view.getChordChange()).thenReturn(chordChange);
        Mockito.when(view.getBeatSpeed()).thenReturn(beatSpeed);
        presenter.setView(view);

        model = Mockito.mock(IPractiseModel.class);
        ((PractisePresenter) presenter).setModel(model);
    }

    @Test
    public void setView_CallsSetToolbarTitleTextOnView() {
        // assert
        Mockito.verify(view).setToolbarTitleText();
    }

    @Test
    public void setModel_CallsLoadSoundOnView() {
        // assert
        Mockito.verify(view).loadSounds();
    }

    @Test
    public void setView_CallsSetFirstChordTextOnViewWithFirstChord() {
        // assert
        Mockito.verify(view).setFirstChordText(selectedChords.get(0));
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
        Mockito.verify(view).setChordText(selectedChords.get(1));
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
    public void viewOnStopPractising_CallsReturnToPractiseSetupOnView() {
        // act
        presenter.viewOnStopPractising();

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
    public void modelOnCountdownFinished_ShowStopButtonOnView() {
        // act
        presenter.modelOnCountdownFinished();

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
}
