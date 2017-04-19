package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.AwaitConditionCreator;
import com.example.jlo19.guitartutor.helpers.FakeChordsCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.models.interfaces.ILearnAllChordsModel;
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
 * Testing LearnAllChordsModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class LearnAllChordsModelTest {

    private ILearnAllChordsModel model;
    private ILearnAllChordsPresenter presenter;
    private int userId;
    private List<Chord> chords;
    private List<Chord> userChords;
    private List<Integer> userChordIds;
    private User user;
    private String apiKey;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new LearnAllChordsModel();

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        apiKey = "api_key";
        Mockito.when(sharedPreferences.getString("api_key", "")).thenReturn(apiKey);
        model.setSharedPreferences(sharedPreferences);

        presenter = PowerMockito.mock(ILearnAllChordsPresenter.class);
        model.setPresenter(presenter);

        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        userChords = Collections.singletonList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
        userChordIds = Collections.singletonList(1);
        user = new User(userId, "Kate", "katesmith@gmail.com", 2, 2000, "api_key");
    }

    @Test
    public void getChordsAndDetails_CallsGetChordsOnApi() {
        // arrange
        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null),
                new FakeChordsCall(null), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.getChordsCalledOnApi(api, apiKey));
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsOnApi_GetAllChords_ReturnsAllChords() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        model.getChordsAndDetails();

        // act
        // allows time for thread
        await().atMost(1000, MILLISECONDS).untilCall(to(model).getAllChords(), is(not(nullValue())));
        final List<Chord> actualChords = model.getAllChords();

        // assert
        Assert.assertEquals(chords, actualChords);
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsOnApi_CallsGetUserChordsOnApiWithUserIdFromSharedPref() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);

        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.getUserChordsCalledOnApi(
                api, userId, apiKey));
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsAndGetUserChordsOnApi_GetUserChordsId_ReturnsUserChords() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        model.getChordsAndDetails();

        // act
        // allows time for thread
        await().atMost(1000, MILLISECONDS).untilCall(to(model).getUserChordIds(), is(not(nullValue())));
        List<Integer> actualUserChords = model.getUserChordIds();

        // assert
        Assert.assertEquals(userChordIds, actualUserChords);
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulGetChordsAndGetUserChordsOnApi_CallsGetAccountDetailsOnApiWithUserIdAndApiKeyFromSharedPref() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);

        final DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.getAccountDetailsCalledOnApi(
                api, userId, apiKey));
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulResponseToAllApiCalls_GetUserLevel_ReturnsUsersLevel() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        Response<User> userResponse = FakeResponseCreator.getUserResponse(true, user);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(userResponse)));
        ((LearnAllChordsModel) model).setApi(api);

        model.getChordsAndDetails();

        // act
        // allows time for thread
        await().atMost(1000, MILLISECONDS).untilCall(to(model).getUserLevel(), is(not(nullValue())));
        int actualUserLevel = model.getUserLevel();

        // assert
        Assert.assertEquals(user.getLevel(), actualUserLevel);
    }

    @Test
    public void getChordsAndDetailsWithSuccessfulResponseToAllApiCalls_CallsChordsRetrievedOnPresenter() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        Response<User> userResponse = FakeResponseCreator.getUserResponse(true, user);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(userResponse)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.
                chordsAndDetailsRetrievedCalledOnPresenter(presenter));
    }

    @Test
    public void getChordsAndDetailsWithUnsuccessfulGetAccountDetailsOnApi_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        Response<User> userResponse = FakeResponseCreator.getUserResponse(false, null);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(userResponse)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnPresenter(presenter));
    }

    @Test
    public void getChordsAndDetailsWithFailureOnGetAccountDetailsOnApi_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnPresenter(presenter));
    }

    @Test
    public void getChordsAndDetailsWithUnsuccessfulGetUserChordsOnApi_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(false,
                null);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(userChordsResponse), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnPresenter(presenter));
    }

    @Test
    public void getChordsAndDetailsWithFailureOnGetUserChordsOnApi_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(true, chords);

        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnPresenter(presenter));
    }

    @Test
    public void getChordsAndDetailsWithUnsuccessfulGetChordsOnApi_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> chordsResponse = FakeResponseCreator.getChordsResponse(false, null);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(chordsResponse),
                new FakeChordsCall(null), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnPresenter(presenter));
    }

    @Test
    public void getChordsAndDetailsWithFailureOnGetChordsOnApi_CallsErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null),
                new FakeChordsCall(null), new FakeUserCall(null)));
        ((LearnAllChordsModel) model).setApi(api);

        // act
        model.getChordsAndDetails();

        // assert
        await().atMost(1000, MILLISECONDS).until(AwaitConditionCreator.errorCalledOnPresenter(presenter));
    }
}
