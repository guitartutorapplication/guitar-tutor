package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.listeners.BeatTimerListener;
import com.example.jlo19.guitartutor.listeners.PractiseActivityTimerListener;

import org.mockito.InOrder;
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

    public static Callable<Boolean> firstRoundOfChordsCalledOnListener(final PractiseActivityTimerListener
                                                                                listener) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(listener).onFirstRoundOfChords();
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> newPractiseStateCalledOnListener(
            final PractiseActivityTimerListener listener, final PractiseActivityState state, final
    VerificationMode verificationMode, final int numChords) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    // ensuring calls are received in order
                    InOrder inOrder = Mockito.inOrder(listener);
                    for (int i = 0; i < numChords; i++) {
                        inOrder.verify(listener, verificationMode).onNewPractiseState(state, i);
                    }
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }
}
