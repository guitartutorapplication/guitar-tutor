package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeChordsResponseCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeUserChordsResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.ILearnAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.UserChord;
import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
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

/**
 * Testing LearnAllChordsModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class LearnAllChordsModelTest {

    private ILearnAllChordsModel model;
    private ILearnAllChordsPresenter presenter;
    private int userId;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new LearnAllChordsModel();

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        model.setSharedPreferences(sharedPreferences);

        presenter = PowerMockito.mock(ILearnAllChordsPresenter.class);
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
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(api).getUserChords(userId);
    }

    @Test
    public void getChords_OnResponseToGetChordsAndGetUserChords_CallsChordsRetrievedOnPresenter() {
        // arrange
        // sets fake call with a response with chords
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        List<UserChord> userChords = Collections.singletonList(new UserChord(1));
        List<Integer> userChordsId = new ArrayList<>();
        for (int i = 0; i < userChords.size(); i++) {
            userChordsId.add(userChords.get(i).getChordId());
        }

        Response<ChordsResponse> chordsResponse = (Response<ChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(chordsResponse.body()).thenReturn(new ChordsResponse(false, chords));

        Response<UserChordsResponse> userChordsResponse = (Response<UserChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userChordsResponse.body()).thenReturn(new UserChordsResponse(userChords));

        DatabaseApi api = new FakeDatabaseApi(new FakeChordsResponseCall(chordsResponse),
                new FakeUserChordsResponseCall(userChordsResponse));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnChordsRetrieved(chords, userChordsId);
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
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getChords_OnFailureToGetChords_CallsErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakeChordsResponseCall call = new FakeChordsResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnError();
    }
}
