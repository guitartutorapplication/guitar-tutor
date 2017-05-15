package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.interactors.interfaces.ILoginInteractor;
import com.example.jlo19.guitartutor.listeners.LoginListener;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Testing LoginInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class, ResponseBody.class})
public class LoginInteractorTest {

    private LoginListener listener;
    private User user;

    @Before
    public void setUp() {
        listener = PowerMockito.mock(LoginListener.class);
        user = new User(1, "Kate", "katesmith@gmail.com", 2, 1000, "api_key");
    }

    @Test
    public void login_CallsLoginOnDatabaseApiWithCredentials() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.loginUser(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mockito.mock(Call.class));

        ILoginInteractor loginInteractor = new LoginInteractor(api);
        loginInteractor.setListener(listener);

        // act
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        loginInteractor.login(expectedEmail, expectedPassword);

        // assert
        Mockito.verify(api).loginUser(expectedEmail, expectedPassword);
    }

    @Test
    public void login_OnSuccessfulResponse_CallsLoginSuccessOnListener() {
        // arrange
        // sets up API with user response that is successful
        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));

        ILoginInteractor loginInteractor = new LoginInteractor(api);
        loginInteractor.setListener(listener);

        // act
        loginInteractor.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(listener).onLoginSuccess(user);
    }

    @Test
    public void login_OnErrorResponse_CallsLoginErrorOnListener()
            throws IOException {
        // arrange
        // sets up API with user response that is unsuccessful
        Response<User> response = FakeResponseCreator.getUserResponse(false, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));

        ILoginInteractor loginInteractor = new LoginInteractor(api);
        loginInteractor.setListener(listener);

        // act
        loginInteractor.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(listener).onLoginError();
    }

    @Test
    public void login_OnFailure_CallsLoginErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(null));
        ILoginInteractor loginInteractor = new LoginInteractor(api);
        loginInteractor.setListener(listener);

        // act
        loginInteractor.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(listener).onLoginError();
    }
}
