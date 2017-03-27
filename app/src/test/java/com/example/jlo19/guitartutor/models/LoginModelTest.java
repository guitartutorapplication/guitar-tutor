package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ResponseError;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakePostResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.models.retrofit.PostResponse;
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
        Response<PostResponse> response = (Response<PostResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(true);

        DatabaseApi api = new FakeDatabaseApi(new FakePostResponseCall(response));
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnLoginSuccess();
    }

    @Test
    public void loginWithValidCredentials_OnIncorrectCredentialsResponse_CallsIncorrectCredentialsOnPresenter()
            throws IOException {
        // arrange
        // sets fake call with an invalid credentials response
        Response<PostResponse> response = (Response<PostResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(false);

        PostResponse postResponse = new PostResponse(true,
                ResponseError.INCORRECT_CREDENTIALS.toString());
        ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
        PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(postResponse));
        PowerMockito.when(response.errorBody()).thenReturn(errorBody);

        DatabaseApi api = new FakeDatabaseApi(new FakePostResponseCall(response));
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
        Response<PostResponse> response = (Response<PostResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(false);

        PostResponse postResponse = new PostResponse(true,
                "another error");
        ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
        PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(postResponse));
        PowerMockito.when(response.errorBody()).thenReturn(errorBody);

        DatabaseApi api = new FakeDatabaseApi(new FakePostResponseCall(response));
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
        FakePostResponseCall call = new FakePostResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((LoginModel) model).setApi(api);

        // act
        model.login("kate@gmail.com", "password");

        // assert
        Mockito.verify(presenter).modelOnLoginError();
    }
}
