package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.views.PractiseSetupView;

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
 * Testing PractiseSetupPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class PractiseSetupPresenterTest {

    private IPractiseSetupPresenter presenter;
    private IPractiseSetupModel model;
    private PractiseSetupView view;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new PractiseSetupPresenter();

        sharedPreferences = Mockito.mock(SharedPreferences.class);
        presenter.setSharedPreferences(sharedPreferences);

        model = Mockito.mock(IPractiseSetupModel.class);
        ((PractiseSetupPresenter) presenter).setModel(model);

        view = Mockito.mock(PractiseSetupView.class);
        presenter.setView(view);
    }

    @Test
    public void setView_CallsLoadSoundOnView() {
        // assert
        Mockito.verify(view).loadSound();
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void setModel_CallsGetChordsOnModel() {
        // assert
        Mockito.verify(model).getChords();
    }

    @Test
    public void setModel_SetsSharedPreferencesOnModel() {
        // assert
        Mockito.verify(model).setSharedPreferences(sharedPreferences);
    }

    @Test
    public void setModel_SetsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void modelOnChordsRetrieved_HidesProgressBarOnView() {
        // act
        presenter.modelOnChordsRetrieved(null);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnChordsRetrieved_SetsChordsOnView() {
        // act
        List<Chord> expectedChords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        presenter.modelOnChordsRetrieved(expectedChords);

        // assert
        Mockito.verify(view).setChords(expectedChords);
    }

    @Test
    public void modelOnLoadChordsError_HidesProgressBarOnView() {
        // act
        presenter.modelOnLoadChordsError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnLoadChordsError_ShowLoadChordsErrorOnView() {
        // act
        presenter.modelOnLoadChordsError();

        // assert
        Mockito.verify(view).showLoadChordsError();
    }

    @Test
    public void viewOnPractise_CallsChordsSelectedOnModel() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
        }};
        int selectedChordChangeIndex = 1;
        int selectedBeatSpeedIndex = 1;
        presenter.viewOnPractise(selectedChords, selectedChordChangeIndex, selectedBeatSpeedIndex);

        // assert
        Mockito.verify(model).chordsSelected(selectedChords, selectedChordChangeIndex, selectedBeatSpeedIndex);
    }

    @Test
    public void modelOnLessThanTwoChordsSelected_CallsLessThanTwoChordsSelectedErrorOnView() {
        // act
        presenter.modelOnLessThanTwoChordsSelected();

        // assert
        Mockito.verify(view).showLessThanTwoChordsSelectedError();
    }

    @Test
    public void modelOnSameSelectedChord_CallsShowSameSelectedChordErrorOnView() {
        // act
        presenter.modelOnSameSelectedChord();

        // assert
        Mockito.verify(view).showSameSelectedChordError();
    }

    @Test
    public void modelOnCorrectSelectedChords_CallsStartPractiseActivityOnView() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
        }};
        presenter.modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT, BeatSpeed.VERY_SLOW);

        // assert
        Mockito.verify(view).startPractiseActivity(selectedChords, ChordChange.ONE_BEAT, BeatSpeed.VERY_SLOW);
    }

    @Test
    public void viewOnBeatPreview_CallsStartBeatPreviewTimerOnModel() {
        // act
        int selectedBeatSpeedIndex = 3;
        presenter.viewOnBeatPreview(selectedBeatSpeedIndex);

        // assert
        Mockito.verify(model).startBeatPreview(selectedBeatSpeedIndex);
    }

    @Test
    public void viewOnBeatPreview_CallsEnablePreviewButtonWithFalseOnView() {
        // act
        presenter.viewOnBeatPreview(3);

        // assert
        Mockito.verify(view).enablePreviewButton(false);
    }

    @Test
    public void modelOnNewBeat_CallsPlaySoundOnView() {
        // act
        presenter.modelOnNewBeat();

        // assert
        Mockito.verify(view).playSound();
    }

    @Test
    public void modelOnPreviewBeatError_CallsShowPreviewBeatErrorOnView() {
        // act
        presenter.modelOnPreviewBeatError();

        // assert
        Mockito.verify(view).showPreviewBeatError();
    }

    @Test
    public void modelOnBeatPreviewFinished_CallsEnablePreviewButtonWithTrueOnView() {
        // act
        presenter.modelOnBeatPreviewFinished();

        // assert
        Mockito.verify(view).enablePreviewButton(true);
    }

    @Test
    public void viewOnBeatSpeedChanged_CallsStopBeatPreviewOnModel() {
        // act
        presenter.viewOnBeatSpeedChanged();

        // assert
        Mockito.verify(model).stopBeatPreview();
    }

    @Test
    public void viewOnDestroy_StopsBeatPreviewOnModel() {
        // act
        presenter.viewOnDestroy();

        // assert
        Mockito.verify(model).stopBeatPreview();
    }

    @Test
    public void viewOnPause_StopsBeatPreviewOnModel() {
        // act
        presenter.viewOnPause();

        // assert
        Mockito.verify(model).stopBeatPreview();
    }

    @Test
    public void viewOnStop_StopsBeatPreviewOnModel() {
        // act
        presenter.viewOnStop();

        // assert
        Mockito.verify(model).stopBeatPreview();
    }
}
