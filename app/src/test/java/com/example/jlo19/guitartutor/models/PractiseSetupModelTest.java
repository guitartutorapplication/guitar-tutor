package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.helpers.FakeChordsResponseCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeUserChordsResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.UserChord;
import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

import static org.mockito.Mockito.never;

/**
 * Testing PractiseSetupModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class PractiseSetupModelTest {

    private IPractiseSetupModel model;
    private IPractiseSetupPresenter presenter;
    private int userId;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new PractiseSetupModel();

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        model.setSharedPreferences(sharedPreferences);

        presenter = PowerMockito.mock(IPractiseSetupPresenter.class);
        model.setPresenter(presenter);
    }

    @Test
    public void getChords_OnResponseToGetChords_CallsGetUserChordsOnApiWithUserIdFromSharedPref() {
        // arrange
        // sets fake call with a response with chords
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        List<UserChord> userChords = Collections.singletonList(new UserChord(1));

        Response<ChordsResponse> chordsResponse = (Response<ChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(chordsResponse.body()).thenReturn(new ChordsResponse(false, chords));

        Response<UserChordsResponse> userChordsResponse = (Response<UserChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userChordsResponse.body()).thenReturn(new UserChordsResponse(userChords));

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsResponseCall(chordsResponse),
                new FakeUserChordsResponseCall(userChordsResponse)));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(api).getUserChords(userId);
    }

    @Test
    public void getChords_OnResponseToGetChordsAndGetUserChords_CallsChordsRetrievedOnPresenter() {
        // arrange
        // sets fake call with a response with chords
        List<Chord> allChords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        List<UserChord> userChords = Collections.singletonList(new UserChord(1));

        Response<ChordsResponse> chordsResponse = (Response<ChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(chordsResponse.body()).thenReturn(new ChordsResponse(false, allChords));

        Response<UserChordsResponse> userChordsResponse = (Response<UserChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userChordsResponse.body()).thenReturn(new UserChordsResponse(userChords));

        DatabaseApi api = new FakeDatabaseApi(new FakeChordsResponseCall(chordsResponse),
                new FakeUserChordsResponseCall(userChordsResponse));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnChordsRetrieved(Collections.singletonList(allChords.get(0)));
    }

    @Test
    public void getChords_OnResponseToGetChordsAndFailureToGetUserChords_CallsErrorOnPresenter() {
        // arrange
        // sets fake call with a response with chords for getChords and fake call with no response
        // for getUserChords (failure)
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));

        Response<ChordsResponse> chordsResponse = (Response<ChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(chordsResponse.body()).thenReturn(new ChordsResponse(false, chords));

        DatabaseApi api = new FakeDatabaseApi(new FakeChordsResponseCall(chordsResponse),
                new FakeUserChordsResponseCall(null));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnLoadChordsError();
    }

    @Test
    public void getChords_OnFailureToGetChords_CallsErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakeChordsResponseCall call = new FakeChordsResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnLoadChordsError();
    }
    @Test
    public void chordsSelected_WithLessThanTwoChords_CallsLessThanTwoChordsSelectedOnPresenter() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(null);
            add(null);
            add(null);
        }};
        model.chordsSelected(selectedChords, 0, 0);

        // assert
        Mockito.verify(presenter).modelOnLessThanTwoChordsSelected();
    }

    @Test
    public void chordsSelected_WithTwoOfTheSameChord_CallsSameSelectedChordOnPresenter() {
        // act
        final Chord repeatedChord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4");
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(repeatedChord);
            add(repeatedChord);
            add(new Chord(1, "C", "MAJOR", "C.png", "C.mp4"));
            add(null);
        }};
        model.chordsSelected(selectedChords, 0, 0);

        // assert
        Mockito.verify(presenter).modelOnSameSelectedChord();
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexZero_CallsCorrectSelectedChordsOnPresenterWithOneBeat() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 0, 0);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT,
                BeatSpeed.VERY_SLOW);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexOne_CallsCorrectSelectedChordsOnPresenterWithTwoBeats() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 1, 0);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.TWO_BEATS,
                BeatSpeed.VERY_SLOW);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexTwo_CallsCorrectSelectedChordsOnPresenterWithFourBeats() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 2, 0);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.FOUR_BEATS,
                BeatSpeed.VERY_SLOW);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexThree_CallsCorrectSelectedChordsOnPresenterWithEightBeats() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 3, 0);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.EIGHT_BEATS,
                BeatSpeed.VERY_SLOW);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexFour_CallsCorrectSelectedChordsOnPresenterWithSixteenBeats() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 4, 0);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.SIXTEEN_BEATS,
                BeatSpeed.VERY_SLOW);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndBeatSpeedIndexZero_CallsCorrectSelectedChordsOnPresenterWithVerySlowSpeed() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 0, 0);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT,
                BeatSpeed.VERY_SLOW);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndBeatSpeedIndexOne_CallsCorrectSelectedChordsOnPresenterWithSlowSpeed() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 0, 1);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT,
                BeatSpeed.SLOW);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndBeatSpeedIndexTwo_CallsCorrectSelectedChordsOnPresenterWithMediumSpeed() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 0, 2);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT,
                BeatSpeed.MEDIUM);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndBeatSpeedIndexThree_CallsCorrectSelectedChordsOnPresenterWithFastSpeed() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 0, 3);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT,
                BeatSpeed.FAST);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndBeatSpeedIndexFour_CallsCorrectSelectedChordsOnPresenterWithVeryFastSpeed() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4"));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4"));
        }};
        model.chordsSelected(selectedChords, 0, 4);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT,
                BeatSpeed.VERY_FAST);
    }

    @Test
    public void startBeatPreviewWithVerySlow_After4Seconds_CallsNewBeatOnPresenterTwice() throws InterruptedException {
        // act
        model.startBeatPreview(0);
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter, Mockito.times(2)).modelOnNewBeat();
    }

    @Test
    public void startBeatPreviewWithSlow_After4Seconds_CallsNewBeatOnPresenterThreeTimes() throws InterruptedException {
        // act
        model.startBeatPreview(1);
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter, Mockito.times(3)).modelOnNewBeat();
    }

    @Test
    public void startBeatPreviewWithMedium_After4Seconds_CallsNewBeatOnPresenterFourTimes() throws InterruptedException {
        // act
        model.startBeatPreview(2);
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter, Mockito.times(4)).modelOnNewBeat();
    }

    @Test
    public void startBeatPreviewWithFast_After4Seconds_CallsNewBeatOnPresenterFiveTimes() throws InterruptedException {
        // act
        model.startBeatPreview(3);
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter, Mockito.times(5)).modelOnNewBeat();
    }

    @Test
    public void startBeatPreviewWithVeryFast_After4Seconds_CallsNewBeatOnPresenterEightTimes() throws InterruptedException {
        // act
        model.startBeatPreview(4);
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter, Mockito.times(8)).modelOnNewBeat();
    }

    @Test
    public void startBeatPreview_After4Seconds_CallsBeatPreviewFinishedOnPresenter() throws InterruptedException {
        // act
        model.startBeatPreview(0);
        Thread.sleep(4000);

        // assert
        Mockito.verify(presenter).modelOnBeatPreviewFinished();
    }

    @Test
    public void startBeatPreview_StopBeatPreview_CallsBeatPreviewFinishedOnPresenter() {
        // arrange
        model.startBeatPreview(0);

        // act
        model.stopBeatPreview();

        // assert
        Mockito.verify(presenter).modelOnBeatPreviewFinished();
    }

    @Test
    public void startBeatPreview_StopBeatPreview_DoesntCallsNewBeatOnPresenter() {
        // arrange
        model.startBeatPreview(0);

        // act
        model.stopBeatPreview();

        // assert
        Mockito.verify(presenter, never()).modelOnNewBeat();
    }

}
