package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.helpers.FakeChordsCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.listeners.GetUserChordsListener;
import com.example.jlo19.guitartutor.models.Chord;
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

import retrofit2.Response;

/**
 * Testing GetUserChordsInteractorTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class GetUserChordsInteractorTest {

    private GetUserChordsListener listener;

    @Before
    public void setUp() {
        listener = PowerMockito.mock(GetUserChordsListener.class);
    }

    @Test
    public void getUserChords_CallsUserChordsOnApi() {
        // arrange
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));
        IGetUserChordsInteractor getUserChordsInteractor = new GetUserChordsInteractor(api);
        getUserChordsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        int userId = 2;
        getUserChordsInteractor.getUserChords(apiKey, userId);

        // assert
        Mockito.verify(api).getUserChords(apiKey, userId);
    }

    @Test
    public void getUserChords_OnSuccessfulResponse_CallsUserChordsRetrievedOnListener() {
        // arrange
        ArrayList<Chord> chords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
        }};
        // sets up API with chords response that is successful
        Response<List<Chord>> response = FakeResponseCreator.getChordsResponse(true, chords);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(response)));

        IGetUserChordsInteractor getUserChordsInteractor = new GetUserChordsInteractor(api);
        getUserChordsInteractor.setListener(listener);

        // act
        getUserChordsInteractor.getUserChords("api_key", 2);

        // assert
        Mockito.verify(listener).onUserChordsRetrieved(chords);
    }

    @Test
    public void getUserChords_OnUnsuccessfulResponse_CallsErrorOnListener() {
        // arrange
        // sets up API with chords response that is unsuccessful
        Response<List<Chord>> response = FakeResponseCreator.getChordsResponse(false, null);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(response)));

        IGetUserChordsInteractor getUserChordsInteractor = new GetUserChordsInteractor(api);
        getUserChordsInteractor.setListener(listener);

        // act
        getUserChordsInteractor.getUserChords("api_key", 2);

        // assert
        Mockito.verify(listener).onGetUserChordsError();
    }

    @Test
    public void getUserChords_OnFailure_CallsErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));
        IGetUserChordsInteractor getUserChordsInteractor = new GetUserChordsInteractor(api);
        getUserChordsInteractor.setListener(listener);

        // act
        getUserChordsInteractor.getUserChords("api_key", 2);

        // assert
        Mockito.verify(listener).onGetUserChordsError();
    }

}
