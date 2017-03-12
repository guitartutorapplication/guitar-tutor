package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.ILearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnViewAllChordsPresenter;
import com.example.jlo19.guitartutor.views.LearnViewAllChordsView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Testing LearnViewAllChordsPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class LearnViewAllChordsPresenterTest {

    private LearnViewAllChordsView view;
    private ILearnViewAllChordsPresenter presenter;
    private ILearnViewAllChordsModel model;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new LearnViewAllChordsPresenter();

        model = Mockito.mock(ILearnViewAllChordsModel.class);
        ((LearnViewAllChordsPresenter) presenter).setModel(model);

        view = Mockito.mock(LearnViewAllChordsView.class);
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
    public void modelOnError_HidesProgressBarOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnError_ShowErrorOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).showError();
    }
}
