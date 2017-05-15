package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.interactors.interfaces.IUpdateUserChordsInteractor;
import com.example.jlo19.guitartutor.listeners.UpdateUserChordsListener;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Testing UpdateUserChordsInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class UpdateUserChordsInteractorTest {

    private UpdateUserChordsListener listener;
    private User user;
    private ArrayList<Integer> chordIds;

    @Before
    public void setUp() {
        listener = Mockito.mock(UpdateUserChordsListener.class);
        user = new User(2, "Kate", "katesmith@gmail.com", 2, 1000, "api_key");
        chordIds = new ArrayList<Integer>() {{
            add(1);
            add(2);
        }};
    }

    @Test
    public void updateUserChords_CallsUpdateUserChordOnApi() {
        // arrange
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(null)));
        IUpdateUserChordsInteractor updateUserChordsInteractor = new UpdateUserChordsInteractor(api);
        updateUserChordsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        int userId = 2;
        updateUserChordsInteractor.updateUserChords(apiKey, userId, chordIds);

        // assert
        Mockito.verify(api).updateUserChords(apiKey, userId, chordIds);
    }

    @Test
    public void updateUserChords_OnSuccessfulResponse_CallsUpdateUserChordsSuccessOnListener() {
        // arrange
        // sets up API with user response that is successful
        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(response)));

        IUpdateUserChordsInteractor updateUserChordsInteractor = new UpdateUserChordsInteractor(api);
        updateUserChordsInteractor.setListener(listener);

        // act
        updateUserChordsInteractor.updateUserChords("api_key", 2, chordIds);

        // assert
        Mockito.verify(listener).onUpdateUserChordsSuccess(user.getLevel(), user.getAchievements());
    }

    @Test
    public void updateUserChords_OnUnsuccessfulResponse_CallsUpdateUserChordsErrorOnListener() {
        // arrange
        // sets up API with user response that is unsuccessful
        Response<User> response = FakeResponseCreator.getUserResponse(false, null);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(response)));

        IUpdateUserChordsInteractor updateUserChordsInteractor = new UpdateUserChordsInteractor(api);
        updateUserChordsInteractor.setListener(listener);

        // act
        updateUserChordsInteractor.updateUserChords("api_key", 2, chordIds);

        // assert
        Mockito.verify(listener).onUpdateUserChordsError();
    }

    @Test
    public void updateUserChords_OnFailure_CallsUpdateUserChordsErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(null)));
        IUpdateUserChordsInteractor updateUserChordsInteractor = new UpdateUserChordsInteractor(api);
        updateUserChordsInteractor.setListener(listener);

        // act
        updateUserChordsInteractor.updateUserChords("api_key", 2, chordIds);

        // assert
        Mockito.verify(listener).onUpdateUserChordsError();
    }
}
