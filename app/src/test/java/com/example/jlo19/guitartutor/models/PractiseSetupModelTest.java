package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.helpers.AwaitConditionCreator;
import com.example.jlo19.guitartutor.helpers.FakeChordsCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

import static org.awaitility.Awaitility.await;
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
    public void chordsSelected_WithLessThanTwoChords_CallsLessThanTwoChordsSelectedOnPresenter() {
        // act
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
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
        final Chord repeatedChord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4","A.wav" , 1);
        ArrayList<Chord> selectedChords = new ArrayList<Chord>() {{
            add(repeatedChord);
            add(repeatedChord);
            add(new Chord(1, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
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
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
        }};
        model.chordsSelected(selectedChords, 0, 4);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT,
                BeatSpeed.VERY_FAST);
    }

    @Test
    public void startBeatPreviewWithVerySlow_CallsNewBeatOnPresenterTwiceAfter4Seconds() {
        // act
        model.startBeatPreview(0);

        // assert
        await().atMost(4000, TimeUnit.MILLISECONDS).until(
                AwaitConditionCreator.newPreviewBeatCalledOnPresenter(presenter, Mockito.times(2)));
    }

    @Test
    public void startBeatPreviewWithSlow_CallsNewBeatOnPresenterThreeTimesAfter4Seconds() {
        // act
        model.startBeatPreview(1);

        // assert
        await().atMost(4000, TimeUnit.MILLISECONDS).until(
                AwaitConditionCreator.newPreviewBeatCalledOnPresenter(presenter, Mockito.times(3)));
    }

    @Test
    public void startBeatPreviewWithMedium_CallsNewBeatOnPresenterFourTimesAfter4Seconds() {
        // act
        model.startBeatPreview(2);

        // assert
        await().atMost(4000, TimeUnit.MILLISECONDS).until(
                AwaitConditionCreator.newPreviewBeatCalledOnPresenter(presenter, Mockito.times(4)));
    }

    @Test
    public void startBeatPreviewWithFast_CallsNewBeatOnPresenterFiveTimesAfter4Seconds() {
        // act
        model.startBeatPreview(3);

        // assert
        await().atMost(4000, TimeUnit.MILLISECONDS).until(
                AwaitConditionCreator.newPreviewBeatCalledOnPresenter(presenter, Mockito.times(5)));
    }

    @Test
    public void startBeatPreviewWithVeryFast_CallsNewBeatOnPresenterEightTimesAfter4Seconds() {
        // act
        model.startBeatPreview(4);

        // assert
        await().atMost(4000, TimeUnit.MILLISECONDS).until(
                AwaitConditionCreator.newPreviewBeatCalledOnPresenter(presenter, Mockito.times(8)));
    }

    @Test
    public void startBeatPreview_CallsBeatPreviewFinishedOnPresenterAfter4Seconds() {
        // act
        model.startBeatPreview(0);

        // assert
        await().atMost(4000, TimeUnit.MILLISECONDS).until(AwaitConditionCreator.
                beatPreviewFinishedCalledOnPresenter(presenter));
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

    @Test
    public void getChords_CallsUserChordsOnApiWithIdFromSharedPreferences() {
        // arrange
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(api).getUserChords(userId);
    }

    @Test
    public void getChords_OnSuccessfulResponse_CallsChordsRetrievedOnPresenter() {
        // arrange
        ArrayList<Chord> chords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
        }};

        Response<List<Chord>> response = FakeResponseCreator.getChordsResponse(true, chords);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(response)));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnChordsRetrieved(chords);
    }

    @Test
    public void getChords_OnUnsuccessfulResponse_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> response = FakeResponseCreator.getChordsResponse(false, null);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(response)));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnLoadChordsError();
    }

    @Test
    public void getChords_OnFailure_CallsErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnLoadChordsError();
    }

}
