package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Testing PractiseModel
 */

public class PractiseModelTest {

    private IPractiseModel model;
    private IPractisePresenter presenter;
    private List<String> selectedChords;

    @Before
    public void setUp() {
        model = new PractiseModel();

        presenter = Mockito.mock(IPractisePresenter.class);
        model.setPresenter(presenter);

        selectedChords = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
            add("D");
        }};
        model.setSelectedChords(selectedChords);

        model.createTimer();
    }

    @Test
    public void start_After32Seconds_CallsNewChordOnPresenterForEachChord() throws InterruptedException {
        // act
        model.start();
        Thread.sleep(32000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void start_After8Seconds_CallsNewSecondOnPresenterForEachSecond() throws InterruptedException {
        // act
        model.start();
        Thread.sleep(8000);

        // assert
        Mockito.verify(presenter, times(8)).modelOnNewSecond();
    }

    @Test
    public void startAndRunFor7Seconds_Stop_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.start();
        Thread.sleep(7000);

        // act
        model.stop();
        Thread.sleep(2000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void startAndRunFor8Seconds_Stop_AfterOneSecond_CallsNewSecondOnPresenterOnly8Times()
            throws InterruptedException {
        // arrange
        model.start();
        Thread.sleep(8000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(8)).modelOnNewSecond();
    }
}
