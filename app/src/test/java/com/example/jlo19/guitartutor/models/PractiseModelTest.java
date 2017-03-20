package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.Countdown;
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

        model.createPractiseTimer();
    }

    @Test
    public void oneBeatChordChange_StartPractiseTimer_After4Seconds_CallsNewChordOnPresenterForEachChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.ONE_BEAT);

        // act
        model.startPractiseTimer();
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void twoBeatsChordChange_StartPractiseTimer_After8Seconds_CallsNewChordOnPresenterForEachChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.TWO_BEATS);

        // act
        model.startPractiseTimer();
        Thread.sleep(8000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void fourBeatsChordChange_StartPractiseTimer_After16Seconds_CallsNewChordOnPresenterForEachChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.FOUR_BEATS);

        // act
        model.startPractiseTimer();
        Thread.sleep(16000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void eightBeatsChordChange_StartPractiseTimer_After32Seconds_CallsNewChordOnPresenterForEachChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.EIGHT_BEATS);

        // act
        model.startPractiseTimer();
        Thread.sleep(32000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void sixteenBeatsChordChange_StartPractiseTimer_After64Seconds_CallsNewChordOnPresenterForEachChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.SIXTEEN_BEATS);

        // act
        model.startPractiseTimer();
        Thread.sleep(64000);

        // assert
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
        Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
    }

    @Test
    public void verySlowBeatSpeed_StartPractiseTimer_After3Seconds_CallsNewBeatOnPresenterTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);

        // act
        model.startPractiseTimer();
        Thread.sleep(3000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void slowBeatSpeed_StartPractiseTimer_After5Seconds_CallsNewBeatOnPresenterFourTimes()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.SLOW);

        // act
        model.startPractiseTimer();
        Thread.sleep(5000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void mediumBeatSpeed_StartPractiseTimer_After2Seconds_CallsNewBeatOnPresenterTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);

        // act
        model.startPractiseTimer();
        Thread.sleep(2000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void fastBeatSpeed_StartPractiseTimer_After3Seconds_CallsNewBeatOnPresenterFourTimes()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.FAST);

        // act
        model.startPractiseTimer();
        Thread.sleep(3000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void veryFastBeatSpeed_StartPractiseTimer_After1Second_CallsNewBeatOnPresenterTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_FAST);

        // act
        model.startPractiseTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void verySlowBeatSpeed_StartPractiseTimerAndRunFor3Seconds_Stop_After2Seconds_CallsNewSecondOnPresenterOnlyTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);
        model.startPractiseTimer();
        Thread.sleep(3000);

        // act
        model.stopTimer();
        Thread.sleep(2000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void slowBeatSpeed_StartPractiseTimerAndRunFor5Seconds_Stop_After2Seconds_CallsNewSecondOnPresenterOnlyFourTimes()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.SLOW);
        model.startPractiseTimer();
        Thread.sleep(5000);

        // act
        model.stopTimer();
        Thread.sleep(2000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void mediumBeatSpeed_StartPractiseTimerAndRunFor2Seconds_Stop_After1Second_CallsNewSecondOnPresenterOnlyTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.MEDIUM);
        model.startPractiseTimer();
        Thread.sleep(2000);

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void fastBeatSpeed_StartPractiseTimerAndRunFor3Seconds_Stop_After1Second_CallsNewSecondOnPresenterOnlyFourTimes()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.FAST);
        model.startPractiseTimer();
        Thread.sleep(3000);

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(4)).modelOnNewBeat();
    }

    @Test
    public void veryFastBeatSpeed_StartPractiseTimerAndRunFor1Second_Stop_After1Second_CallsNewSecondOnPresenterOnlyTwice()
            throws InterruptedException {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_FAST);
        model.startPractiseTimer();
        Thread.sleep(1000);

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, times(2)).modelOnNewBeat();
    }

    @Test
    public void oneBeatChordChange_StartPractiseTimer_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.ONE_BEAT);
        model.startPractiseTimer();

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void twoBeatsChordChange_StartPractiseTimerAndRunFor1Second_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.TWO_BEATS);
        model.startPractiseTimer();
        Thread.sleep(1000);

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void fourBeatsChordChange_StartPractiseTimerAndRunFor3Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.FOUR_BEATS);
        model.startPractiseTimer();
        Thread.sleep(3000);

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void eightBeatsChordChange_StartPractiseTimerAndRunFor7Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.EIGHT_BEATS);
        model.startPractiseTimer();
        Thread.sleep(7000);

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void sixteenBeatsChordChange_StartPractiseTimerAndRunFor15Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord()
            throws InterruptedException {
        // arrange
        model.setChordChange(ChordChange.SIXTEEN_BEATS);
        model.startPractiseTimer();
        Thread.sleep(15000);

        // act
        model.stopTimer();
        Thread.sleep(1000);

        // assert
        Mockito.verify(presenter, never()).modelOnNewChord(selectedChords.get(1));
    }

    @Test
    public void startCountdown_After6Seconds_CallsNewSecondOfCountdownForEachOnPresenter()
            throws InterruptedException {
        // act
        model.startCountdown();
        Thread.sleep(6000);

        // assert
        Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.THREE);
        Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.TWO);
        Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.ONE);
        Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.GO);
    }

    @Test
    public void startCountdown_After8Seconds_CallsCountdownFinishedOnPresenter()
            throws InterruptedException {
        // act
        model.startCountdown();
        Thread.sleep(8000);

        // assert
        Mockito.verify(presenter).modelOnCountdownFinished();
    }

    @Test
    public void startCountdown_Stop_DoesntCallsNewSecondOnPresenter() {
        // arrange
        model.startCountdown();

        // act
        model.stopTimer();

        // assert
        Mockito.verify(presenter, never()).modelOnNewSecondOfCountdown((Countdown) Mockito.any());
    }

    @Test
    public void startCountdown_Stop_DoesntCallsCountdownFinishedOnPresenter() {
        // arrange
        model.startCountdown();

        // act
        model.stopTimer();

        // assert
        Mockito.verify(presenter, never()).modelOnCountdownFinished();
    }

}
