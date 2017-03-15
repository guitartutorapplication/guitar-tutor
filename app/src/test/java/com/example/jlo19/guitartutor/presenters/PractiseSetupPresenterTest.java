package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
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

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new PractiseSetupPresenter();

        model = Mockito.mock(IPractiseSetupModel.class);
        ((PractiseSetupPresenter) presenter).setModel(model);

        view = Mockito.mock(PractiseSetupView.class);
        presenter.setView(view);
    }

    @Test
    public void setView_CallsSetToolbarTitleTextOnView() {
        // assert
        Mockito.verify(view).setToolbarTitleText();
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void setView_CallsGetChordsOnModel() {
        // assert
        Mockito.verify(model).getChords();
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
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
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
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("C");
            add("B");
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
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("C");
            add("B");
        }};
        presenter.modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT, BeatSpeed.VERY_SLOW);

        // assert
        Mockito.verify(view).startPractiseActivity(selectedChords, ChordChange.ONE_BEAT, BeatSpeed.VERY_SLOW);
    }
}
