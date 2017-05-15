package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.timers.interfaces.IBeatTimer;
import com.example.jlo19.guitartutor.views.PractiseSetupView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Testing PractiseSetupPresenter
 */
public class PractiseSetupPresenterTest {

    private IPractiseSetupPresenter presenter;
    private PractiseSetupView view;
    private LoggedInUser loggedInUser;
    private IGetUserChordsInteractor getUserChordsInteractor;
    private IBeatTimer beatTimer;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        Mockito.when(loggedInUser.getUserId()).thenReturn(2);
        Mockito.when(loggedInUser.getApiKey()).thenReturn("api_key");
        getUserChordsInteractor = Mockito.mock(IGetUserChordsInteractor.class);
        beatTimer = Mockito.mock(IBeatTimer.class);

        presenter = new PractiseSetupPresenter(getUserChordsInteractor, loggedInUser, beatTimer);

        view = Mockito.mock(PractiseSetupView.class);
        presenter.setView(view);
    }

    @Test
    public void setView_CallsLoadSoundOnView() {
        // assert
        Mockito.verify(view).loadSound();
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void callsGetChordsOnInteractor() {
        // assert
        Mockito.verify(getUserChordsInteractor).getUserChords(loggedInUser.getApiKey(),
                loggedInUser.getUserId());
    }

    @Test
    public void setsListenerOnInteractor() {
        // assert
        Mockito.verify(getUserChordsInteractor).setListener(presenter);
    }

    @Test
    public void setsListenerOnBeatTimer() {
        // assert
        Mockito.verify(beatTimer).setListener(presenter);
    }

    @Test
    public void onChordsRetrieved_HidesProgressBarOnView() {
        // act
        presenter.onUserChordsRetrieved(null);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onChordsRetrieved_SetsChordsOnView() {
        // act
        List<Chord> expectedChords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        presenter.onUserChordsRetrieved(expectedChords);

        // assert
        Mockito.verify(view).setChords(expectedChords);
    }

    @Test
    public void onGetChordsError_HidesProgressBarOnView() {
        // act
        presenter.onGetUserChordsError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onGetChordsError_ShowLoadChordsErrorOnView() {
        // act
        presenter.onGetUserChordsError();

        // assert
        Mockito.verify(view).showLoadChordsError();
    }

    @Test
    public void viewOnPractiseWithValidChordsSelected_CallsStartPractiseActivityOnView() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
        }};
        int selectedChordChangeIndex = 1;
        int selectedBeatSpeedIndex = 1;
        presenter.viewOnPractise(selectedChords, selectedChordChangeIndex, selectedBeatSpeedIndex);

        // assert
        Mockito.verify(view).startPractiseActivity(selectedChords, ChordChange.values()[selectedChordChangeIndex],
                BeatSpeed.values()[selectedBeatSpeedIndex]);
    }

    @Test
    public void viewOnPractiseWithLessThanTwoChordsSelected_CallsLessThanTwoChordsSelectedErrorOnView() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
        }};
        int selectedChordChangeIndex = 1;
        int selectedBeatSpeedIndex = 1;
        presenter.viewOnPractise(selectedChords, selectedChordChangeIndex, selectedBeatSpeedIndex);

        // assert
        Mockito.verify(view).showLessThanTwoChordsSelectedError();
    }

    @Test
    public void viewOnPractiseWithSameSelectedChord_CallsShowSameSelectedChordErrorOnView() {
        // act
        final Chord chord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1);
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(chord);
            add(chord);
        }};
        int selectedChordChangeIndex = 1;
        int selectedBeatSpeedIndex = 1;
        presenter.viewOnPractise(selectedChords, selectedChordChangeIndex, selectedBeatSpeedIndex);

        // assert
        Mockito.verify(view).showSameSelectedChordError();
    }

    @Test
    public void viewOnBeatPreview_CallsStartOnBeatTimer() {
        // act
        int selectedBeatSpeedIndex = 3;
        presenter.viewOnBeatPreview(selectedBeatSpeedIndex);

        // assert
        Mockito.verify(beatTimer).start(BeatSpeed.values()[selectedBeatSpeedIndex]);
    }

    @Test
    public void viewOnBeatPreview_CallsEnablePreviewButtonWithFalseOnView() {
        // act
        presenter.viewOnBeatPreview(3);

        // assert
        Mockito.verify(view).enablePreviewButton(false);
    }

    @Test
    public void onNewBeat_CallsPlaySoundOnView() {
        // act
        presenter.onNewBeat(1);

        // assert
        Mockito.verify(view).playSound();
    }

    @Test
    public void onBeatTimerFinished_EnablesPreviewButtonOnView() {
        // act
        presenter.onBeatTimerFinished();

        // assert
        Mockito.verify(view).enablePreviewButton(true);
    }

    @Test
    public void onTimerError_CallsShowPreviewBeatErrorOnView() {
        // act
        presenter.onBeatTimerError();

        // assert
        Mockito.verify(view).showPreviewBeatError();
    }

    @Test
    public void viewOnBeatSpeedChanged_CallsStopOnBeatTimer() {
        // act
        presenter.viewOnBeatSpeedChanged();

        // assert
        Mockito.verify(beatTimer).stop();
    }

    @Test
    public void viewOnDestroy_CallsStopOnBeatTimer() {
        // act
        presenter.viewOnDestroy();

        // assert
        Mockito.verify(beatTimer).stop();
    }

    @Test
    public void viewOnPause_CallsStopOnBeatTimer() {
        // act
        presenter.viewOnPause();

        // assert
        Mockito.verify(beatTimer).stop();
    }

    @Test
    public void viewOnStop_CallsStopOnBeatTimer() {
        // act
        presenter.viewOnStop();

        // assert
        Mockito.verify(beatTimer).stop();
    }

    @Test
    public void viewOnConfirmError_CallFinishActivityOnView() {
        // act
        presenter.viewOnConfirmError();

        // assert
        Mockito.verify(view).finishActivity();
    }
}
