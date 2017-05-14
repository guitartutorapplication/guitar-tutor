package com.example.jlo19.guitartutor.timers;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.helpers.AwaitConditionCreator;
import com.example.jlo19.guitartutor.listeners.BeatTimerListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.never;

/**
 * Testing BeatTimer
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Thread.class)
public class BeatTimerTest {

    private BeatTimer beatTimer;
    private BeatTimerListener listener;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Thread.class);
        listener = PowerMockito.mock(BeatTimerListener.class);

        beatTimer = new BeatTimer();
        beatTimer.setListener(listener);
    }

    @Test
    public void start_CallsListenerOnNewBeat() {
        // act
        beatTimer.start(BeatSpeed.FAST);

        // assert
        await().until(AwaitConditionCreator.newBeatIsCalledOnListener(listener, 1));
    }

    @Test
    public void start_CallsSleepOnThreadWithBeatSpeedValue() throws InterruptedException {
        // act
        BeatSpeed beatSpeed = BeatSpeed.FAST;
        beatTimer.start(beatSpeed);

        // assert
        await().until(AwaitConditionCreator.sleepIsCalledOnThread(beatSpeed.getValue()));
    }

    @Test
    public void start_CallStopAfter1Beat_NewBeatIsNotCalledOnListenerWith2() {
        // arrange
        beatTimer.start(BeatSpeed.FAST);
        // waiting for first beat to be called
        await().until(AwaitConditionCreator.newBeatIsCalledOnListener(listener, 1));

        // act
        beatTimer.stop();

        // assert
        Mockito.verify(listener, never()).onNewBeat(2);
    }

    @Test
    public void start_CallStopAfter1Beat_SleepIsNotCalledOnThread() throws InterruptedException {
        // arrange
        BeatSpeed beatSpeed = BeatSpeed.FAST;
        beatTimer.start(beatSpeed);
        // waiting for first beat to be called
        await().until(AwaitConditionCreator.newBeatIsCalledOnListener(listener, 1));

        // act
        beatTimer.stop();

        // assert
        PowerMockito.verifyStatic(never());
        Thread.sleep(beatSpeed.getValue());
    }
}
