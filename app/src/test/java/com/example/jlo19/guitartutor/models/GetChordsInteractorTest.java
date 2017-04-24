package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.helpers.AwaitConditionCreator;
import com.example.jlo19.guitartutor.helpers.FakeChordsCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.listeners.GetChordsListener;
import com.example.jlo19.guitartutor.models.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
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

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.to;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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

    @Before
    public void setUp() {
        listener = PowerMockito.mock(ILearnAllChordsPresenter.class);

        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        userChords = Collections.singletonList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
        userChordIds = Collections.singletonList(1);
        user = new User(2, "Kate", "katesmith@gmail.com", 2, 2000, "api_key");
    }

    @Test
    public void getChordsAndDetails_CallsGetChordsOnApi() {
        // arrange
        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null),
                new FakeChordsCall(null), new FakeUserCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        getChordsInteractor.getChordsAndDetails(apiKey, user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.getChordsCalledOnApi(api, apiKey));
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsOnApi_GetAllChords_ReturnsAllChords() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // act
        // allows time for thread
        await().atMost(1000, MILLISECONDS).untilCall(to(getChordsInteractor).getAllChords(), is(not(nullValue())));
        final List<Chord> actualChords = getChordsInteractor.getAllChords();

        // assert
        Assert.assertEquals(chords, actualChords);
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsOnApi_CallsGetUserChordsOnApi() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);

        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        getChordsInteractor.getChordsAndDetails(apiKey, user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.getUserChordsCalledOnApi(
                api, user.getId(), apiKey));
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsAndGetUserChordsOnApi_GetUserChordsId_ReturnsUserChords() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // act
        // allows time for thread
        await().atMost(1000, MILLISECONDS).untilCall(to(getChordsInteractor).getUserChordIds(),
                is(not(nullValue())));
        List<Integer> actualUserChords = getChordsInteractor.getUserChordIds();

        // assert
        Assert.assertEquals(userChordIds, actualUserChords);
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsAndGetUserChordsOnApi_CallsGetAccountDetailsOnApi() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);

        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        getChordsInteractor.getChordsAndDetails(apiKey, user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.getAccountDetailsCalledOnApi(
                api, user.getId(), apiKey));
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulResponseToAllApiCalls_CallsChordsRetrievedOnListener() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        Response<User> userResponse = FakeResponseCreator.getUserResponse(true, user);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(userResponse)));
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.
                chordsAndDetailsRetrievedCalledOnListener(listener, chords, user.getLevel(), userChordIds));
    }

    @Test
    public void getChordsAndDetailsWithUnsuccessfulGetAccountDetailsOnApi_CallsErrorOnListener() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        Response<User> userResponse = FakeResponseCreator.getUserResponse(false, null);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(userResponse)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnListener(listener));
    }

    @Test
    public void getChordsAndDetailsWithFailureOnGetAccountDetailsOnApi_CallsErrorOnListener() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnListener(listener));
    }

    @Test
    public void getChordsAndDetailsWithUnsuccessfulGetUserChordsOnApi_CallsErrorOnListener() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(false,
                null);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnListener(listener));
    }

    @Test
    public void getChordsAndDetailsWithFailureOnGetUserChordsOnApi_CallsErrorOnListener() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);

        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnListener(listener));
    }

    @Test
    public void getChordsAndDetailsWithUnsuccessfulGetChordsOnApi_CallsErrorOnListener() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(false, null);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));
        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnListener(listener));
    }

    @Test
    public void getChordsAndDetailsWithFailureOnGetChordsOnApi_CallsErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null),
                new FakeChordsCall(null), new FakeUserCall(null)));

        IGetChordsInteractor getChordsInteractor = new GetChordsInteractor(api);
        getChordsInteractor.setListener(listener);

        // act
        getChordsInteractor.getChordsAndDetails("api_key", user.getId());

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnListener(listener));
    }
}
