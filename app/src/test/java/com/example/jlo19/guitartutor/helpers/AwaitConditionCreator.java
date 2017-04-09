package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.enums.Countdown;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import java.util.List;
import java.util.concurrent.Callable;

import static org.mockito.Mockito.never;

/**
 * All possible test conditions that Awaitility must wait for to be true, used so multithreaded code can be tested
 */
public class AwaitConditionCreator {

    public static Callable<Boolean> getChordsCalledOnApi(final DatabaseApi api) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(api).getChords();
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> getUserChordsCalledOnApi(final DatabaseApi api, final int userId) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(api).getUserChords(userId);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> getAccountDetailsCalledOnApi(final DatabaseApi api, final int userId) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(api).getAccountDetails(userId);
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

    public static Callable<Boolean> newChordCalledOnPresenterForEachChord(
            final IPractisePresenter presenter, final List<Chord> selectedChords) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter).modelOnNewChord(selectedChords.get(0));
                    Mockito.verify(presenter).modelOnNewChord(selectedChords.get(1));
                    Mockito.verify(presenter).modelOnNewChord(selectedChords.get(2));
                    Mockito.verify(presenter).modelOnNewChord(selectedChords.get(3));
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> newBeatCalledOnPresenter(
            final IPractisePresenter presenter, final VerificationMode verificationMode) {
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

    public static Callable<Boolean> newChordNotCalledOnPresenterForChord(
            final IPractisePresenter presenter, final Chord chord) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter, never()).modelOnNewChord(chord);
                    return true;
                } catch (AssertionError error) {
                    return false;
                }
            }
        };
    }

    public static Callable<Boolean> newSecondOfCountdownCalledOnPresenterForEach(
            final IPractisePresenter presenter) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.THREE);
                    Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.TWO);
                    Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.ONE);
                    Mockito.verify(presenter).modelOnNewSecondOfCountdown(Countdown.GO);
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
