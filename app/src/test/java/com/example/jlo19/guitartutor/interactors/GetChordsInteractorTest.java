package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.helpers.FakeChordsCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.listeners.GetChordsListener;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

/**
 * Testing GetChordsInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class GetChordsInteractorTest {

    private GetChordsListener listener;
    private List<Chord> chords;
    private List<Chord> userChords;
    private List<Integer> userChordIds;
    private User user;
    private IGetUserChordsInteractor getUserChordsInteractor;
    private IGetAccountDetailsInteractor getAccountDetailsInteractor;

    @Before
    public void setUp() {
        listener = PowerMockito.mock(GetChordsListener.class);

        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        userChords = Collections.singletonList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
        userChordIds = Collections.singletonList(1);
        user = new User(2, "Kate", "katesmith@gmail.com", 2, 2000, "api_key");

        getUserChordsInteractor = Mockito.mock(IGetUserChordsInteractor.class);
        getAccountDetailsInteractor = Mockito.mock(IGetAccountDetailsInteractor.class);
    }

    @Test
    public void getChordsAndDetails_CallsGetChordsOnApi() {
        // arrange
        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api,
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        getChordsInteractor.getChordsAndDetails(apiKey, user.getId());

        // assert
        Mockito.verify(api).getChords(apiKey);
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsOnApi_GetAllChords_ReturnsAllChords() {
        // arrange
        // sets up API with chords response that is successful
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api,
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // act
        final List<Chord> actualChords = getChordsInteractor.getAllChords();

        // assert
        Assert.assertEquals(chords, actualChords);
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsOnApi_CallsGetUserChordsOnInteractor() {
        // arrange
        // sets up API with chords response that is successful
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api,
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        getChordsInteractor.getChordsAndDetails(apiKey, user.getId());

        // assert
        Mockito.verify(getUserChordsInteractor).getUserChords(apiKey, user.getId());
    }

    @Test
    public void onUserChordsRetrieved_GetUserChordsId_ReturnsUserChords() {
        // arrange
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(Mockito.mock(DatabaseApi.class),
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);
        getChordsInteractor.onUserChordsRetrieved(userChords);

        // act
        List<Integer> actualUserChords = getChordsInteractor.getUserChordIds();

        // assert
        Assert.assertEquals(userChordIds, actualUserChords);
    }

    @Test
    public void getChordsAndDetails_onUserChordsRetrieved_CallsGetAccountDetailsOnInteractor() {
        // arrange
        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api,
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        String apiKey = "api_key";
        getChordsInteractor.getChordsAndDetails(apiKey, user.getId());

        // act
        getChordsInteractor.onUserChordsRetrieved(userChords);

        // assert
        Mockito.verify(getAccountDetailsInteractor).getAccountDetails(apiKey, user.getId());
    }

    @Test
    public void onAccountDetailsRetrieved_CallsChordsAndDetailsRetrievedOnListener() {
        // arrange
        // sets up API with chords response that is successful
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api,
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        getChordsInteractor.getChordsAndDetails("api_key", user.getId());
        getChordsInteractor.onUserChordsRetrieved(userChords);

        // act
        getChordsInteractor.onAccountDetailsRetrieved(user);

        // assert
        Mockito.verify(listener).onChordsAndDetailsRetrieved(chords, user.getLevel(), userChordIds);
    }

    @Test
    public void onGetAccountDetailsError_CallsErrorOnListener() {
        // arrange
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(Mockito.mock(DatabaseApi.class),
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.onGetAccountDetailsError();

        // assert
        Mockito.verify(listener).onError();
    }

    @Test
    public void onGetUserChordsError_CallsErrorOnListener() {
        // arrange
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(Mockito.mock(DatabaseApi.class),
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.onGetUserChordsError();

        // assert
        Mockito.verify(listener).onError();
    }

    @Test
    public void getChordsAndDetailsWithUnsuccessfulGetChordsOnApi_CallsErrorOnListener() {
        // arrange
        // sets up API with chords response that is unsuccessful
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(false, null);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api,
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        Mockito.verify(listener).onError();
    }

    @Test
    public void getChordsAndDetailsWithFailureOnGetChordsOnApi_CallsErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api,
                getUserChordsInteractor, getAccountDetailsInteractor);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        Mockito.verify(listener).onError();
    }
}
