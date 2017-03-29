package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ResponseError;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeLoginResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.models.retrofit.LoginResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.google.gson.Gson;

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
 * Testing LoginModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class, ResponseBody.class})
public class LoginModelTest {

    private ILoginModel model;
    private ILoginPresenter presenter;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new LoginModel();

        presenter = PowerMockito.mock(ILoginPresenter.class);
        model.setPresenter(presenter);
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
        // sets fake call with a successful response
        LoginResponse loginResponse = new LoginResponse(false, "success", 1);

        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);

        model.setSharedPreferences(sharedPreferences);

        Response<LoginResponse> response = (Response<LoginResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.body()).thenReturn(loginResponse);
        PowerMockito.when(response.isSuccessful()).thenReturn(true);

        DatabaseApi api = new FakeDatabaseApi(new FakeLoginResponseCall(response));
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
        int userId = 1;
        LoginResponse loginResponse = new LoginResponse(false, "success", userId);

        SharedPreferences sharedPreferences = Mockito.spy(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);

        model.setSharedPreferences(sharedPreferences);

        Response<LoginResponse> response = (Response<LoginResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.body()).thenReturn(loginResponse);
        PowerMockito.when(response.isSuccessful()).thenReturn(true);

        DatabaseApi api = new FakeDatabaseApi(new FakeLoginResponseCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(editor).putInt("user_id", userId);
        Mockito.verify(editor).apply();
    }

    @Test
    public void loginWithValidCredentials_OnIncorrectCredentialsResponse_CallsIncorrectCredentialsOnPresenter()
            throws IOException {
        // arrange
        // sets fake call with an invalid credentials response
        Response<LoginResponse> response = (Response<LoginResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(false);

        LoginResponse loginResponse = new LoginResponse(true,
                ResponseError.INCORRECT_CREDENTIALS.toString(), 0);
        ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
        PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(loginResponse));
        PowerMockito.when(response.errorBody()).thenReturn(errorBody);

        DatabaseApi api = new FakeDatabaseApi(new FakeLoginResponseCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnIncorrectCredentials();
    }

    @Test
    public void loginWithValidCredentials_OnErrorResponse_CallsLoginErrorOnPresenter()
            throws IOException {
        // arrange
        // sets fake call with a different error response
        Response<LoginResponse> response = (Response<LoginResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(false);

        LoginResponse loginResponse = new LoginResponse(true,
                "another error", 0);
        ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
        PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(loginResponse));
        PowerMockito.when(response.errorBody()).thenReturn(errorBody);

        DatabaseApi api = new FakeDatabaseApi(new FakeLoginResponseCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnLoginError();
    }

    @Test
    public void loginWithValidCredentials_OnFailure_CallsLoginErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakeLoginResponseCall call = new FakeLoginResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnLoginError();
    }
}
