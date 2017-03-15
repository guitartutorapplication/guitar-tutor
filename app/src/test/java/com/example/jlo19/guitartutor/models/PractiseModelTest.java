package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
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
        model.setChordChange(ChordChange.ONE_BEAT);
        model.setBeatSpeed(BeatSpeed.MEDIUM);

        model.createTimer();
    }

    @Test
    public void oneBeatChordChange_start_After4Seconds_CallsNewChordOnPresenterForEachChord() throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.ONE_BEAT);

        // act
        model.start();
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void twoBeatsChordChange_start_After8Seconds_CallsNewChordOnPresenterForEachChord() throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.TWO_BEATS);

        // act
        model.start();
        Thread.sleep(8000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void fourBeatsChordChange_start_After16Seconds_CallsNewChordOnPresenterForEachChord() throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.FOUR_BEATS);

        // act
        model.start();
        Thread.sleep(16000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void eightBeatsChordChange_start_After32Seconds_CallsNewChordOnPresenterForEachChord() throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.EIGHT_BEATS);

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
    public void sixteenBeatsChordChange_start_After64Seconds_CallsNewChordOnPresenterForEachChord() throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.SIXTEEN_BEATS);

        // act
        model.start();
        Thread.sleep(64000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void verySlowBeatSpeed_Start_After3Seconds_CallsNewBeatOnPresenterTwice() throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);

        // act
        model.start();
        Thread.sleep(3000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void slowBeatSpeed_Start_After5Seconds_CallsNewBeatOnPresenterFourTimes() throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.SLOW);

        // act
        model.start();
        Thread.sleep(5000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void mediumBeatSpeed_Start_After2Seconds_CallsNewBeatOnPresenterTwice() throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);

        // act
        model.start();
        Thread.sleep(2000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void fastBeatSpeed_Start_After3Seconds_CallsNewBeatOnPresenterFourTimes() throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.FAST);

        // act
        model.start();
        Thread.sleep(3000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void veryFastBeatSpeed_Start_After1Second_CallsNewBeatOnPresenterTwice() throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_FAST);

        // act
        model.start();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void verySlowBeatSpeed_StartAndRunFor3Seconds_Stop_After2Seconds_CallsNewSecondOnPresenterOnlyTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);
        model.start();
        Thread.sleep(3000);

        // act
        model.stop();
        Thread.sleep(2000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void slowBeatSpeed_StartAndRunFor5Seconds_Stop_After2Seconds_CallsNewSecondOnPresenterOnlyFourTimes()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.SLOW);
        model.start();
        Thread.sleep(5000);

        // act
        model.stop();
        Thread.sleep(2000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void mediumBeatSpeed_StartAndRunFor2Seconds_Stop_After1Second_CallsNewSecondOnPresenterOnlyTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.MEDIUM);
        model.start();
        Thread.sleep(2000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void fastBeatSpeed_StartAndRunFor3Seconds_Stop_After1Second_CallsNewSecondOnPresenterOnlyFourTimes()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.FAST);
        model.start();
        Thread.sleep(3000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void veryFastBeatSpeed_StartAndRunFor1Second_Stop_After1Second_CallsNewSecondOnPresenterOnlyTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_FAST);
        model.start();
        Thread.sleep(1000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void oneBeatChordChange_Start_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.ONE_BEAT);
        model.start();

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void twoBeatsChordChange_StartAndRunFor1Second_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.TWO_BEATS);
        model.start();
        Thread.sleep(1000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void fourBeatsChordChange_StartAndRunFor3Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.FOUR_BEATS);
        model.start();
        Thread.sleep(3000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void eightBeatsChordChange_StartAndRunFor7Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.EIGHT_BEATS);
        model.start();
        Thread.sleep(7000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void sixteenBeatsChordChange_StartAndRunFor15Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.SIXTEEN_BEATS);
        model.start();
        Thread.sleep(15000);

        // act
        model.stop();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }
}
