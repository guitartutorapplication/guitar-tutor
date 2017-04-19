package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.Callable;

/**
 * All possible test conditions that Awaitility must wait for to be true, used so multithreaded code can be tested
 */
public class AwaitConditionCreator {

    public static Callable<Boolean> getChordsCalledOnApi(final DatabaseApi api, final String apiKey) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(api).getChords(apiKey);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> getUserChordsCalledOnApi(
            final DatabaseApi api, final int userId, final String apiKey) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(api).getUserChords(apiKey, userId);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> getAccountDetailsCalledOnApi(
            final DatabaseApi api, final int userId, final String apiKey) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(api).getAccountDetails(apiKey, userId);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> chordsAndDetailsRetrievedCalledOnPresenter(
            final ILearnAllChordsPresenter presenter) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter).modelOnChordsAndDetailsRetrieved();
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> errorCalledOnPresenter(final ILearnAllChordsPresenter presenter) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter).modelOnError();
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

    public static Callable<Boolean> newPreviewBeatCalledOnPresenter(
            final IPractiseSetupPresenter presenter, final VerificationMode verificationMode) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter, verificationMode).modelOnNewBeat();
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> beatPreviewFinishedCalledOnPresenter(
            final IPractiseSetupPresenter presenter) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter).modelOnBeatPreviewFinished();
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
