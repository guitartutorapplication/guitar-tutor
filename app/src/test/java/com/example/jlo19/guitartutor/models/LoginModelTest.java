package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
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

import static org.mockito.Mockito.never;

/**
 * Testing LoginModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class, ResponseBody.class})
public class LoginModelTest {

    private ILoginModel model;
    private ILoginPresenter presenter;
    private User user;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new LoginModel();

        presenter = PowerMockito.mock(ILoginPresenter.class);
        model.setPresenter(presenter);

        user = new User(1, "Kate", "katesmith@gmail.com", 2, 1000, "api_key");
    }

    @Test
    public void checkForPreexistingUser_UserAlreadyLoggedIn_CallsUserAlreadyLoggedInOnPresenter() {
        // arrange
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(2);
        model.setSharedPreferences(sharedPreferences);

        // act
        model.checkForPreexistingLogIn();

        // arrange
        Mockito.verify(presenter).modelOnUserAlreadyLoggedIn();
    }

    @Test
    public void checkForPreexistingUser_NoExistingUser_DoesntCallUserAlreadyLoggedInOnPresenter() {
        // arrange
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(0);
        model.setSharedPreferences(sharedPreferences);

        // act
        model.checkForPreexistingLogIn();

        // arrange
        Mockito.verify(presenter, never()).modelOnUserAlreadyLoggedIn();
    }

    @Test
    public void loginWithEmptyEmailField_CallsEmptyFieldOnPresenter() {
        // act
        model.login("", "password");

        // assert
        Mockito.verify(presenter).modelOnFieldEmpty();
    }

    @Test
    public void loginWithEmptyPasswordField_CallsEmptyFieldOnPresenter() {
        // act
        model.login("kate@gmail.com", "");

        // assert
        Mockito.verify(presenter).modelOnFieldEmpty();
    }

    @Test
    public void loginWithValidCredentials_CallsLoginOnDatabaseApiWithCredentials() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.loginUser(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mockito.mock(Call.class));
        ((LoginModel) model).setApi(api);

        // act
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        model.login(expectedEmail, expectedPassword);

        // assert
        Mockito.verify(api).loginUser(expectedEmail, expectedPassword);
    }

    @Test
    public void loginWithValidCredentials_OnSuccessfulResponse_CallsLoginSuccessOnPresenter() {
        // arrange
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);
        model.setSharedPreferences(sharedPreferences);

        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnLoginSuccess();
    }

    @Test
    public void loginWithValidCredentials_OnSuccessfulResponse_AddsUserIdToSharedPreferences() {
        // arrange
        // sets fake call with a successful response
        SharedPreferences sharedPreferences = Mockito.spy(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);
        model.setSharedPreferences(sharedPreferences);

        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login(user.getEmail(), "password");

        // assert
        Mockito.verify(editor).putInt("user_id", user.getId());
        Mockito.verify(editor).apply();
    }

    @Test
    public void loginWithValidCredentials_OnSuccessfulResponse_AddsApiKeyToSharedPreferences() {
        // arrange
        // sets fake call with a successful response
        SharedPreferences sharedPreferences = Mockito.spy(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);
        model.setSharedPreferences(sharedPreferences);

        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login(user.getEmail(), "password");

        // assert
        Mockito.verify(editor).putString("api_key", user.getApiKey());
        Mockito.verify(editor).apply();
    }

    @Test
    public void loginWithValidCredentials_OnErrorResponse_CallsLoginErrorOnPresenter()
            throws IOException {
        // arrange
        Response<User> response = FakeResponseCreator.getUserResponse(false, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnLoginError();
    }

    @Test
    public void loginWithValidCredentials_OnFailure_CallsLoginErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(null));
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnLoginError();
    }
}
