package com.example.jlo19.guitartutor.presenters;

import android.view.View;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ChordChange;
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

import static org.mockito.Mockito.atLeast;

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
        view = Mockito.mock(PractiseView.class);
        Mockito.when(view.getSelectedChords()).thenReturn(selectedChords);
        Mockito.when(view.getChordChange()).thenReturn(chordChange);
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
    public void setView_CallsSetChordTextOnViewWithFirstSelectedChord() {
        // assert
        Mockito.verify(view).setChordText(selectedChords.get(0));
    }

    @Test
    public void setView_CallsLoadSoundOnView() {
        // assert
        Mockito.verify(view).loadSound();
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
    public void setModel_CallsCreateTimerOnModel() {
        // assert
        Mockito.verify(model).createTimer();
    }

    @Test
    public void modelOnNewChord_CallsSetChordTextOnViewWithChord() {
        // act
        presenter.modelOnNewChord(selectedChords.get(1));

        // assert
        Mockito.verify(view).setChordText(selectedChords.get(1));
    }

    @Test
    public void viewOnStartPractising_CallsStartOnModel() {
        // act
        presenter.viewOnStartPractising();

        // assert
        Mockito.verify(model).start();
    }

    @Test
    public void viewOnStartPractising_CallsSetStopButtonVisibilityOnViewWithVisible() {
        // act
        presenter.viewOnStartPractising();

        // assert
        Mockito.verify(view).setStopButtonVisibility(View.VISIBLE);
    }

    @Test
    public void viewOnStartPractising_CallsSetStartButtonVisibilityOnViewWithInvisible() {
        // act
        presenter.viewOnStartPractising();

        // assert
        Mockito.verify(view).setStartButtonVisibility(View.INVISIBLE);
    }


    @Test
    public void viewOnStopPractising_CallsStopOnModel() {
        // act
        presenter.viewOnStopPractising();

        // assert
        Mockito.verify(model).stop();
    }

    @Test
    public void viewOnStopPractising_CallsSetStopButtonVisibilityOnViewWithInvisible() {
        // act
        presenter.viewOnStopPractising();

        // assert
        Mockito.verify(view).setStopButtonVisibility(View.INVISIBLE);
    }

    @Test
    public void viewOnStopPractising_CallsSetStartButtonVisibilityOnViewWithVisible() {
        // act
        presenter.viewOnStopPractising();

        // assert
        Mockito.verify(view).setStartButtonVisibility(View.VISIBLE);
    }

    @Test
    public void viewOnStopPractising_CallsSetChordTextOnViewWithFirstSelectedChord() {
        // act
        presenter.viewOnStopPractising();

        // assert
        Mockito.verify(view, atLeast(1)).setChordText(selectedChords.get(0));
    }

    @Test
    public void modelOnNewSecond_CallsPlaySoundOnView() {
        // act
        presenter.modelOnNewSecond();

        // assert
        Mockito.verify(view).playSound();
    }

    @Test
    public void modelOnError_CallsShowErrorOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void modelOnError_CallsStartPractiseSetupActivity() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).startPractiseSetupActivity();
    }
}
