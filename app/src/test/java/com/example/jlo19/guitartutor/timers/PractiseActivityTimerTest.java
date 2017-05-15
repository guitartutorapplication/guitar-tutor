package com.example.jlo19.guitartutor.timers;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.helpers.AwaitConditionCreator;
import com.example.jlo19.guitartutor.listeners.PractiseActivityTimerListener;
import com.example.jlo19.guitartutor.timers.interfaces.IPractiseActivityTimer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Testing PractiseActivityTimer
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Thread.class)
public class PractiseActivityTimerTest {

    private IPractiseActivityTimer practiseActivityTimer;
    private PractiseActivityTimerListener listener;

    @Before
    public void setUp() throws InterruptedException {
        PowerMockito.mockStatic(Thread.class);
        listener = PowerMockito.mock(PractiseActivityTimerListener.class);

        practiseActivityTimer = new PractiseActivityTimer();
        practiseActivityTimer.setListener(listener);
    }

    @Test
    public void start_CallsFirstRoundOfChordsOnListener() {
        // act
        practiseActivityTimer.start(BeatSpeed.MEDIUM, ChordChange.ONE_BEAT, 3);

        // assert
        await().until(AwaitConditionCreator.firstRoundOfChordsCalledOnListener(listener));
    }

    @Test
    public void start_CallsNewChordOnListenerForEachChord() {
        // act
        int numChords = 4;
        practiseActivityTimer.start(BeatSpeed.MEDIUM, ChordChange.ONE_BEAT, numChords);

        // assert
        await().until(AwaitConditionCreator.
                newPractiseStateCalledOnListener(listener, PractiseActivityState.NEW_CHORD, times(1),
                        numChords));
    }

    @Test
    public void startWithOneBeatChordChange_CallsNewBeatOnListenerOnceForEachChord() {
        // act
        int numChords = 4;
        practiseActivityTimer.start(BeatSpeed.MEDIUM, ChordChange.ONE_BEAT, numChords);

        // assert
        await().until(AwaitConditionCreator.
                newPractiseStateCalledOnListener(listener, PractiseActivityState.NEW_BEAT, times(1),
                        numChords));
    }

    @Test
    public void startWithTwoBeatChordChange_CallsNewBeatOnListenerTwiceForEachChord() {
        // act
        int numChords = 4;
        practiseActivityTimer.start(BeatSpeed.MEDIUM, ChordChange.TWO_BEATS, numChords);

        // assert
        await().until(AwaitConditionCreator.
                newPractiseStateCalledOnListener(listener, PractiseActivityState.NEW_BEAT, times(2),
                        numChords));
    }

    @Test
    public void startWithFourBeatChordChange_CallsNewBeatOnListenerFourTimesForEachChord() {
        // act
        int numChords = 4;
        practiseActivityTimer.start(BeatSpeed.MEDIUM, ChordChange.FOUR_BEATS, numChords);

        // assert
        await().atMost(17000, TimeUnit.MILLISECONDS).until(AwaitConditionCreator.
                newPractiseStateCalledOnListener(listener, PractiseActivityState.NEW_BEAT, times(4),
                        numChords));
    }

    @Test
    public void startWithEightBeatChordChange_CallsNewBeatOnListenerEightTimesForEachChord() {
        // act
        int numChords = 2;
        practiseActivityTimer.start(BeatSpeed.MEDIUM, ChordChange.EIGHT_BEATS, numChords);

        // assert
        await().atMost(17000, TimeUnit.MILLISECONDS).until(AwaitConditionCreator.
                newPractiseStateCalledOnListener(listener, PractiseActivityState.NEW_BEAT, times(8),
                        numChords));
    }

    @Test
    public void startWithSixteenBeatChordChange_CallsNewBeatOnListenerSixteenTimesForEachChord() {
        // act
        int numChords = 2;
        practiseActivityTimer.start(BeatSpeed.MEDIUM, ChordChange.SIXTEEN_BEATS, numChords);

        // assert
        await().atMost(33000, TimeUnit.MILLISECONDS).until(AwaitConditionCreator.
                newPractiseStateCalledOnListener(listener, PractiseActivityState.NEW_BEAT, times(16),
                        numChords));
    }

    @Test
    public void startWithVerySlowBeatSpeed_CallsSleepOnThreadWithBeatSpeedValue() {
        // act
        BeatSpeed beatSpeed = BeatSpeed.VERY_SLOW;
        practiseActivityTimer.start(beatSpeed, ChordChange.ONE_BEAT, 1);

        // assert
        await().until(AwaitConditionCreator.sleepIsCalledOnThread(beatSpeed.getValue()));
    }

    @Test
    public void startWithSlowBeatSpeed_CallsSleepOnThreadWithBeatSpeedValue() {
        // act
        BeatSpeed beatSpeed = BeatSpeed.SLOW;
        practiseActivityTimer.start(beatSpeed, ChordChange.ONE_BEAT, 1);

        // assert
        await().until(AwaitConditionCreator.sleepIsCalledOnThread(beatSpeed.getValue()));
    }

    @Test
    public void startWithMediumBeatSpeed_CallsSleepOnThreadWithBeatSpeedValue() {
        // act
        BeatSpeed beatSpeed = BeatSpeed.MEDIUM;
        practiseActivityTimer.start(beatSpeed, ChordChange.ONE_BEAT, 1);

        // assert
        await().until(AwaitConditionCreator.sleepIsCalledOnThread(beatSpeed.getValue()));
    }

    @Test
    public void startWithFastBeatSpeed_CallsSleepOnThreadWithBeatSpeedValue() {
        // act
        BeatSpeed beatSpeed = BeatSpeed.FAST;
        practiseActivityTimer.start(beatSpeed, ChordChange.ONE_BEAT, 1);

        // assert
        await().until(AwaitConditionCreator.sleepIsCalledOnThread(beatSpeed.getValue()));
    }

    @Test
    public void startWithVeryFastBeatSpeed_CallsSleepOnThreadWithBeatSpeedValue() {
        // act
        BeatSpeed beatSpeed = BeatSpeed.VERY_FAST;
        practiseActivityTimer.start(beatSpeed, ChordChange.ONE_BEAT, 1);

        // assert
        await().until(AwaitConditionCreator.sleepIsCalledOnThread(beatSpeed.getValue()));
    }

    @Test
    public void start_CallStopAfter1Beat_NewChordIsNotCalledOnListenerWithNextChord() {
        // arrange
        int numChords = 2;
        practiseActivityTimer.start(BeatSpeed.VERY_SLOW, ChordChange.ONE_BEAT, numChords);
        // waiting for first beat to be called
        await().until(AwaitConditionCreator.newPractiseStateCalledOnListener(listener,
                PractiseActivityState.NEW_BEAT, times(1), 1));

        // act
        practiseActivityTimer.stop();

        // assert
        Mockito.verify(listener, never()).onNewPractiseState(PractiseActivityState.NEW_CHORD, 1);
    }

    @Test
    public void start_CallStopAfter1Beat_NewBeatIsNotCalledOnListenerWithNextChord() {
        // arrange
        int numChords = 2;
        practiseActivityTimer.start(BeatSpeed.VERY_SLOW, ChordChange.ONE_BEAT, numChords);
        // waiting for first beat to be called
        await().until(AwaitConditionCreator.newPractiseStateCalledOnListener(listener,
                PractiseActivityState.NEW_BEAT, times(1), 1));

        // act
        practiseActivityTimer.stop();

        // assert
        Mockito.verify(listener, never()).onNewPractiseState(PractiseActivityState.NEW_BEAT, 1);
    }

    @Test
    public void start_CallStopAfter1Beat_DoesntCallSleepOnThreadWithBeatSpeedValue() throws InterruptedException {
        // arrange
        int numChords = 2;
        BeatSpeed beatSpeed = BeatSpeed.MEDIUM;
        practiseActivityTimer.start(beatSpeed, ChordChange.ONE_BEAT, numChords);
        // waiting for first beat to be called
        await().until(AwaitConditionCreator.newPractiseStateCalledOnListener(listener,
                PractiseActivityState.NEW_BEAT, times(1), 1));

        // act
        practiseActivityTimer.stop();

        // assert
        PowerMockito.verifyStatic(never());
        Thread.sleep(beatSpeed.getValue());
    }
}
