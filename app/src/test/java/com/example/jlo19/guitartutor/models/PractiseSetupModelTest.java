package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.helpers.FakeCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
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
import java.util.List;

import retrofit2.Response;

/**
 * Testing PractiseSetupModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class PractiseSetupModelTest {

    private IPractiseSetupModel model;
    private IPractiseSetupPresenter presenter;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new PractiseSetupModel();

        presenter = PowerMockito.mock(IPractiseSetupPresenter.class);
        model.setPresenter(presenter);
    }

    @Test
    public void getChords_OnResponse_CallsChordsRetrievedOnPresenterWithChords() {
        // arrange
        // sets fake call with a response with chords
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        ChordsResponse chordsResponse = new ChordsResponse(false, chords);

        Response<ChordsResponse> response = (Response<ChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.body()).thenReturn(chordsResponse);

        DatabaseApi api = new FakeDatabaseApi(new FakeCall(response));
        ((PractiseSetupModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnChordsRetrieved(chords);
    }

    @Test
    public void getChords_OnFailure_CallsErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakeCall call = new FakeCall(null);
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
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
        }};
        model.chordsSelected(selectedChords, 0);

        // assert
        Mockito.verify(presenter).modelOnLessThanTwoChordsSelected();
    }

    @Test
    public void chordsSelected_WithTwoOfTheSameChord_CallsSameSelectedChordOnPresenter() {
        // act
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("A");
            add("B");
        }};
        model.chordsSelected(selectedChords, 0);

        // assert
        Mockito.verify(presenter).modelOnSameSelectedChord();
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexZero_CallsCorrectSelectedChordsOnPresenterWithOneBeat() {
        // act
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("C");
            add("B");
        }};
        model.chordsSelected(selectedChords, 0);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.ONE_BEAT);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexOne_CallsCorrectSelectedChordsOnPresenterWithTwoBeats() {
        // act
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("C");
            add("B");
        }};
        model.chordsSelected(selectedChords, 1);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.TWO_BEATS);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexTwo_CallsCorrectSelectedChordsOnPresenterWithFourBeats() {
        // act
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("C");
            add("B");
        }};
        model.chordsSelected(selectedChords, 2);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.FOUR_BEATS);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexThree_CallsCorrectSelectedChordsOnPresenterWithEightBeats() {
        // act
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("C");
            add("B");
        }};
        model.chordsSelected(selectedChords, 3);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.EIGHT_BEATS);
    }

    @Test
    public void chordsSelected_WithCorrectSelectedChordsAndChordChangeIndexFour_CallsCorrectSelectedChordsOnPresenterWithSixteenBeats() {
        // act
        ArrayList<String> selectedChords = new ArrayList<String>() {{
            add("A");
            add("C");
            add("B");
        }};
        model.chordsSelected(selectedChords, 4);

        // assert
        Mockito.verify(presenter).modelOnCorrectSelectedChords(selectedChords, ChordChange.SIXTEEN_BEATS);
    }
}
