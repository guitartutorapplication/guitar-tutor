package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.enums.PractiseActivityState;
import com.example.jlo19.guitartutor.helpers.AwaitConditionCreator;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Testing PractiseModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class PractiseModelTest {

    private IPractiseModel model;
    private IPractisePresenter presenter;
    private List<Chord> selectedChords;
    private int userId;
    private User user;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new PractiseModel();

        presenter = Mockito.mock(IPractisePresenter.class);
        model.setPresenter(presenter);

        selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
            add(new Chord(4, "D", "MAJOR", "D.png", "D.mp4", "D.wav", 1));
        }};
        model.setSelectedChords(selectedChords);
        model.setChordChange(ChordChange.ONE_BEAT);
        model.setBeatSpeed(BeatSpeed.MEDIUM);

        model.createPractiseTimer();

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        model.setSharedPreferences(sharedPreferences);

        user = new User(userId, "Kate", "katesmith@gmail.com", 2, 1000);
    }

    @Test
    public void startPractiseTimer_CallsFirstRoundOfChordsOnPresenterAfter5Seconds() {
        // act
        model.startPractiseTimer();

        // assert
        await().atMost(5000, MILLISECONDS).until(AwaitConditionCreator.
                firstRoundOfChordsCalledOnPresenter(presenter));
    }

    @Test
    public void oneBeatChordChange_StartPractiseTimer_CallsNewChordOnPresenterForEachChordAfter4Seconds() {
        // arrange
        model.setChordChange(ChordChange.ONE_BEAT);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < selectedChords.size(); i++) {
            await().atMost(4000, MILLISECONDS).until(AwaitConditionCreator.
                    newPractiseStateCalledOnPresenter(presenter, PractiseActivityState.NEW_CHORD, i,
                            times(1)));
        }
    }

    @Test
    public void twoBeatsChordChange_StartPractiseTimer_CallsNewChordOnPresenterForEachChordAfter8Seconds() {
        // arrange
        model.setChordChange(ChordChange.TWO_BEATS);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < selectedChords.size(); i++) {
            await().atMost(8000, MILLISECONDS).until(AwaitConditionCreator.
                    newPractiseStateCalledOnPresenter(presenter, PractiseActivityState.NEW_CHORD, i,
                            times(1)));
        }
    }

    @Test
    public void fourBeatsChordChange_StartPractiseTimer_CallsNewChordOnPresenterForEachChordAfter16Seconds() {
        // arrange
        model.setChordChange(ChordChange.FOUR_BEATS);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < selectedChords.size(); i++) {
            await().atMost(16000, MILLISECONDS).until(AwaitConditionCreator.
                    newPractiseStateCalledOnPresenter(presenter, PractiseActivityState.NEW_CHORD, i,
                            times(1)));
        }
    }

    @Test
    public void eightBeatsChordChange_StartPractiseTimer_CallsNewChordOnPresenterForEachChordAfter32Seconds() {
        // arrange
        model.setChordChange(ChordChange.EIGHT_BEATS);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < selectedChords.size(); i++) {
            await().atMost(32000, MILLISECONDS).until(AwaitConditionCreator.
                    newPractiseStateCalledOnPresenter(presenter, PractiseActivityState.NEW_CHORD, i,
                            times(1)));
        }
    }

    @Test
    public void sixteenBeatsChordChange_StartPractiseTimer_CallsNewChordOnPresenterForEachChordAfter64Seconds() {
        // arrange
        model.setChordChange(ChordChange.SIXTEEN_BEATS);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < selectedChords.size(); i++) {
            await().atMost(64000, MILLISECONDS).until(AwaitConditionCreator.
                    newPractiseStateCalledOnPresenter(presenter, PractiseActivityState.NEW_CHORD, i,
                            times(1)));
        }
    }

    @Test
    public void verySlowBeatSpeed_StartPractiseTimer_CallsNewBeatOnPresenterTwiceAfter3Seconds() {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < 2; i++) {
            await().atMost(1500, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }
    }

    @Test
    public void slowBeatSpeed_StartPractiseTimer_CallsNewBeatOnPresenterFourTimesAfter5Seconds() {
        // arrange
        model.setBeatSpeed(BeatSpeed.SLOW);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < 4; i++) {
            await().atMost(5000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }
    }

    @Test
    public void mediumBeatSpeed_StartPractiseTimer_CallsNewBeatOnPresenterTwiceAfter2Seconds() {
        // arrange
        model.setBeatSpeed(BeatSpeed.MEDIUM);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < 2; i++) {
            await().atMost(2000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }
    }

    @Test
    public void fastBeatSpeed_StartPractiseTimer_CallsNewBeatOnPresenterFourTimesAfter3Seconds() {
        // arrange
        model.setBeatSpeed(BeatSpeed.FAST);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < 4; i++) {
            await().atMost(3000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }
    }

    @Test
    public void veryFastBeatSpeed_StartPractiseTimer_CallsNewBeatOnPresenterTwiceAfter1Seconds() {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_FAST);

        // act
        model.startPractiseTimer();

        // assert
        for (int i = 0; i < 2; i++) {
            await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }
    }

    @Test
    public void verySlowBeatSpeed_StartPractiseTimerAndRunFor3Seconds_Stop_NewSecondOnPresenterNotCalledAfterStop() {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_SLOW);
        model.startPractiseTimer();
        // waits until new beat is called twice (which is running for 3 seconds)
        for (int i = 0; i < 2; i++) {
            await().atMost(3000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }

        // act
        model.stopTimer();

        // assert
        // waits at least 2 seconds to prove new beat is never called
        await().atLeast(2000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new beat
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, never()));
    }

    @Test
    public void slowBeatSpeed_StartPractiseTimerAndRunFor5Seconds_Stop_NewSecondOnPresenterNotCalledAfterStop() {
        // arrange
        model.setBeatSpeed(BeatSpeed.SLOW);
        model.startPractiseTimer();
        // waits until new beat is called four times (which is running for 5 seconds)
        for (int i = 0; i < 4; i++) {
            await().atMost(5000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }

        // act
        model.stopTimer();

        // assert
        // waits at least 2 seconds to prove new beat is never called
        await().atLeast(2000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new beat
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, never()));
    }

    @Test
    public void mediumBeatSpeed_StartPractiseTimerAndRunFor2Seconds_Stop_NewSecondOnPresenterNotCalledAfterStop() {
        // arrange
        model.setBeatSpeed(BeatSpeed.MEDIUM);
        model.startPractiseTimer();
        // waits until new beat is called twice (which is running for 2 seconds)
        for (int i = 0; i < 2; i++) {
            await().atMost(2000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new beat is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new beat
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, never()));
    }

    @Test
    public void fastBeatSpeed_StartPractiseTimerAndRunFor3Seconds_Stop_NewSecondOnPresenterNotCalledAfterStop() {
        // arrange
        model.setBeatSpeed(BeatSpeed.FAST);
        model.startPractiseTimer();
        // waits until new beat is called four times (which is running for 3 seconds)
        for (int i = 0; i < 4; i++) {
            await().atMost(3000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new beat is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new beat
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, never()));
    }

    @Test
    public void veryFastBeatSpeed_StartPractiseTimerAndRunFor1Second_Stop_NewSecondOnPresenterNotCalledAfterStop() {
        // arrange
        model.setBeatSpeed(BeatSpeed.VERY_FAST);
        model.startPractiseTimer();
        // waits until new beat is called twice (which is running for 1 second)
        for (int i = 0; i < 2; i++) {
            await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                    presenter, PractiseActivityState.NEW_BEAT, i, times(1)));
        }

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new beat is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new beat
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, never()));
    }

    @Test
    public void oneBeatChordChange_StartPractiseTimer_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord() {
        // arrange
        model.setChordChange(ChordChange.ONE_BEAT);
        model.startPractiseTimer();

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new chord is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new chord
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_CHORD, 1, never()));
    }

    @Test
    public void twoBeatsChordChange_StartPractiseTimerAndRunFor1Second_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord() {
        // arrange
        model.setChordChange(ChordChange.TWO_BEATS);
        model.startPractiseTimer();
        // waits until new beat is called once (which is running for 1 seconds)
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, times(1)));

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new chord is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new chord
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_CHORD, 1, never()));
    }

    @Test
    public void fourBeatsChordChange_StartPractiseTimerAndRunFor3Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord() {
        // arrange
        model.setChordChange(ChordChange.FOUR_BEATS);
        model.startPractiseTimer();
        // waits until new beat is called once (which is running for 3 seconds)
        await().atMost(3000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, times(3)));

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new chord is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new chord
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_CHORD, 1, never()));
    }

    @Test
    public void eightBeatsChordChange_StartPractiseTimerAndRunFor7Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord() {
        // arrange
        model.setChordChange(ChordChange.EIGHT_BEATS);
        model.startPractiseTimer();
        // waits until new beat is called seven times (which is running for 7 seconds)
        await().atMost(7000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, times(7)));

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new chord is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new chord
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_CHORD, 1, never()));
    }

    @Test
    public void sixteenBeatsChordChange_StartPractiseTimerAndRunFor15Seconds_Stop_After1Second_DoesNotCallNewChordOnPresenterWithSecondChord() {
        // arrange
        model.setChordChange(ChordChange.SIXTEEN_BEATS);
        model.startPractiseTimer();
        // waits until new beat is called 15 times (which is running for 15 seconds)
        await().atMost(15000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_BEAT, 0, times(15)));

        // act
        model.stopTimer();

        // assert
        // waits at least 1 second to prove new chord is never called
        await().atLeast(1000, MILLISECONDS);
        // resets presenter so doesn't count previous calls to new chord
        Mockito.reset(presenter);
        await().until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.NEW_CHORD, 1, never()));
    }

    @Test
    public void startCountdown_CallsNewSecondOfCountdownForEachOnPresenterAfter6Seconds() {
        // act
        model.startCountdown();

        // assert
        await().atMost(6000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.COUNTDOWN_STAGE_3, 0, times(1)));
        await().atMost(6000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.COUNTDOWN_STAGE_2, 0, times(1)));
        await().atMost(6000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.COUNTDOWN_STAGE_1, 0, times(1)));
        await().atMost(6000, MILLISECONDS).until(AwaitConditionCreator.newPractiseStateCalledOnPresenter(
                presenter, PractiseActivityState.COUNTDOWN_STAGE_GO, 0, times(1)));
    }

    @Test
    public void startCountdown_CallsCountdownFinishedOnPresenterAfter8Seconds()
            throws InterruptedException {
        // act
        model.startCountdown();

        // assert
        await().atMost(8000, MILLISECONDS).until(AwaitConditionCreator
                .countdownFinishedCalledOnPresenter(presenter));
    }

    @Test
    public void startCountdown_Stop_DoesntCallsNewSecondOnPresenter() {
        // arrange
        model.startCountdown();

        // act
        model.stopTimer();

        // assert
        Mockito.verify(presenter, never()).modelOnNewPractiseState((PractiseActivityState)
                Mockito.any(), Mockito.anyInt());
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

    @Test
    public void savePractiseSession_CallsUpdateUserChordOnApi() {
        // arrange
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(null)));
        ((PractiseModel) model).setApi(api);

        // act
        model.savePractiseSession();

        // assert
        ArrayList<Integer> chordIds = new ArrayList<>();
        for (Chord chord : selectedChords) {
            chordIds.add(chord.getId());
        }
        Mockito.verify(api).updateUserChords(userId, chordIds);
    }

    @Test
    public void savePractiseSession_OnSuccessfulResponse_CallsPractiseSessionSavedWithSuccessOnPresenter() {
        // arrange
        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(response)));
        ((PractiseModel) model).setApi(api);

        // act
        model.savePractiseSession();

        // assert
        Mockito.verify(presenter).modelOnPractiseSessionSaved(user.getLevel(), user.getAchievements());
    }

    @Test
    public void savePractiseSession_OnUnsuccessfulResponse_CallsPractiseSessionSavedWithErrorOnPresenter() {
        // arrange
        Response<User> response = FakeResponseCreator.getUserResponse(false, null);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(response)));
        ((PractiseModel) model).setApi(api);

        // act
        model.savePractiseSession();

        // assert
        Mockito.verify(presenter).modelOnPractiseSessionSaveError();
    }

    @Test
    public void savePractiseSession_OnFailure_CallsPractiseSessionSavedWithErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(null)));
        ((PractiseModel) model).setApi(api);

        // act
        model.savePractiseSession();

        // assert
        Mockito.verify(presenter).modelOnPractiseSessionSaveError();
    }

    @Test
    public void getAudioFilenames_ReturnsFilenames() {
        // act
        List<String> actualFilenames = model.getAudioFilenames();

        // assert
        List<String> expectedFilenames = new ArrayList<>();
        for (PractiseActivityState state : PractiseActivityState.values()) {
            if (state == PractiseActivityState.NEW_CHORD) {
                for (Chord chord : selectedChords) {
                    expectedFilenames.add(chord.getAudioFilename());
                }
            }
            else {
                expectedFilenames.add(state.getFilename());
            }
        }
        Assert.assertEquals(expectedFilenames, actualFilenames);
    }
}
