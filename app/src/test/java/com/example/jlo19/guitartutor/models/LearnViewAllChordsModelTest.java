package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.models.interfaces.ILearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnViewAllChordsPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

/**
 * Testing LearnViewAllChordsModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class LearnViewAllChordsModelTest {

    private ILearnViewAllChordsModel model;
    private ILearnViewAllChordsPresenter presenter;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new LearnViewAllChordsModel();

        presenter = PowerMockito.mock(ILearnViewAllChordsPresenter.class);
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
        ((LearnViewAllChordsModel) model).setApi(api);

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
        ((LearnViewAllChordsModel) model).setApi(api);

        // act
        model.getChords();

        // assert
        Mockito.verify(presenter).modelOnError();
    }
}
