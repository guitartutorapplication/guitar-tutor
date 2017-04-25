package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.listeners.AddUserChordListener;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Testing AddUserChordInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class, ResponseBody.class})
public class AddUserChordInteractorTest {

    private AddUserChordListener listener;
    private User user;

    @Before
    public void setUp() {
        listener = Mockito.mock(ILearnChordPresenter.class);
        user = new User(2, "Kate", "katesmith@gmail.com", 2, 2000, "api_key");
    }

    @Test
    public void addUserChord_CallsAddUserChordOnApi() {
        // arrange
        int chordId = 3;
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(null)));

        AddUserChordInteractor addUserChordInteractor = new AddUserChordInteractor(api);
        addUserChordInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        int userId = 2;
        addUserChordInteractor.addUserChord(apiKey, userId, chordId);

        // assert
        Mockito.verify(api).addUserChord(apiKey, userId, chordId);
    }

    @Test
    public void addUserChord_OnSuccessfulResponse_CallsChordAddedOnListener() {
        // arrange
        Response<User> userResponse = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(userResponse));

        AddUserChordInteractor addUserChordInteractor = new AddUserChordInteractor(api);
        addUserChordInteractor.setListener(listener);

        // act
        addUserChordInteractor.addUserChord("api_key", 2, 3);

        // arrange
        Mockito.verify(listener).onChordAdded(user.getLevel(), user.getAchievements());
    }

    @Test
    public void addUserChord_OnUnsuccessfulResponse_CallsAddChordErrorOnListener() {
        // arrange
        Response<User> userResponse = FakeResponseCreator.getUserResponse(false, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(userResponse));

        AddUserChordInteractor addUserChordInteractor = new AddUserChordInteractor(api);
        addUserChordInteractor.setListener(listener);

        // act
        addUserChordInteractor.addUserChord("api_key", 2, 3);

        // assert
        Mockito.verify(listener).onAddChordError();
    }

    @Test
    public void addUserChord_OnFailure_CallsAddsChordErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(null));
        AddUserChordInteractor addUserChordInteractor = new AddUserChordInteractor(api);
        addUserChordInteractor.setListener(listener);

        // act
        addUserChordInteractor.addUserChord("api_key", 2, 3);

        // assert
        Mockito.verify(listener).onAddChordError();
    }
}
