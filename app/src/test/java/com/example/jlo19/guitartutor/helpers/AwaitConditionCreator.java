package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.listeners.BeatTimerListener;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;

import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;

import java.util.concurrent.Callable;

/**
 * All possible test conditions that Awaitility must wait for to be true, used so multithreaded code can be tested
 */
public class AwaitConditionCreator {

    public static Callable<Boolean> newBeatIsCalledOnListener(
            final BeatTimerListener listener, final int numOfBeats) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(listener).onNewBeat(numOfBeats);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> sleepIsCalledOnThread(final int beatSpeedValue) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    PowerMockito.verifyStatic();
                    Thread.sleep(beatSpeedValue);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> firstRoundOfChordsCalledOnPresenter(final IPractisePresenter
                                                                                presenter) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter).modelOnFirstRoundOfChords();
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> newPractiseStateCalledOnPresenter(
            final IPractisePresenter presenter, final PractiseActivityState state, final int
            currentChordIndex, final VerificationMode verificationMode) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter, verificationMode).modelOnNewPractiseState(state, currentChordIndex);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> countdownFinishedCalledOnPresenter(
            final IPractisePresenter presenter) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter).modelOnCountdownFinished();
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }
}
