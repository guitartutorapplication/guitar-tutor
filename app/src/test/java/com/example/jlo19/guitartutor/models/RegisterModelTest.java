package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ResponseError;
import com.example.jlo19.guitartutor.enums.ValidationResult;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakePostPutResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.models.retrofit.PostPutResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
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
 * Testing RegisterModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, ResponseBody.class, Response.class})
public class RegisterModelTest {

    private IRegisterModel model;
    private IRegisterPresenter presenter;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new RegisterModel();

        presenter = PowerMockito.mock(IRegisterPresenter.class);
        model.setPresenter(presenter);
    }

    @Test
    public void registerWithEmptyNameField_CallsEmptyFieldOnPresenter() {
        // act
        model.register("", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void registerWithEmptyEmailField_CallsEmptyFieldOnPresenter() {
        // act
        model.register("Kate", "", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void registerWithEmptyConfirmEmailField_CallsEmptyFieldOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void registerWithEmptyPasswordField_CallsEmptyFieldOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void registerWithEmptyConfirmPasswordField_CallsEmptyFieldOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void registerWithTwoDifferentEmails_CallsEmailMismatchOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "katet@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.EMAIL_MISMATCH);
    }

    @Test
    public void registerWithTwoDifferentEmails_CallsPasswordMismatchOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Passwrod123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_MISMATCH);
    }

    @Test
    public void registerWithInvalidEmail_CallsInvalidEmailOnPresenter() {
        // act
        model.register("Kate", "kate@gmailcom", "kate@gmailcom", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.INVALID_EMAIL);
    }

    @Test
    public void registerWithPasswordLessThanEightCharacters_CallsPasswordTooShortOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "pass", "pass");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_TOO_SHORT);
    }

    @Test
    public void registerWithPasswordWithNoUpperCaseLetter_CallsPasswordNoUpperCaseLetterOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "password123", "password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_UPPER);
    }

    @Test
    public void registerWithPasswordWithNoLowerCaseLetter_CallsPasswordNoLowerCaseLetterOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "PASSWORD123", "PASSWORD123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_LOWER);
    }

    @Test
    public void registerWithPasswordWithNoNumber_CallsPasswordNoNumberOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password", "Password");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_NUMBER);
    }

    @Test
    public void registerWithCorrectCredentials_CallsRegisterOnDatabaseApiWithCredentials() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.registerUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mockito.mock(Call.class));
        ((RegisterModel) model).setApi(api);

        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        model.register(expectedName, expectedEmail, expectedEmail, expectedPassword, expectedPassword);

        // assert
        Mockito.verify(api).registerUser(expectedName, expectedEmail, expectedPassword);
    }

    @Test
    public void registerWithCorrectCredentials_OnSuccessfulResponse_CallsRegisterSuccessOnPresenter() {
        // arrange
        // sets fake call with a successful response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(true);

        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnRegisterSuccess();
    }

    @Test
    public void registerWithCorrectCredentials_OnInvalidEmailResponse_CallsInvalidEmailOnPresenter()
            throws IOException {
        // arrange
        // sets fake call with a invalid email response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(false);

        PostPutResponse postPutResponse = new PostPutResponse(true,
                ResponseError.INVALID_EMAIL.toString());
        ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
        PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(postPutResponse));
        PowerMockito.when(response.errorBody()).thenReturn(errorBody);

        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.INVALID_EMAIL);
    }

    @Test
    public void registerWithCorrectCredentials_OnAlreadyRegisteredResponse_CallsAlreadyRegisterOnPresenter()
            throws IOException {
        // arrange
        // sets fake call with an already registered response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(false);

        PostPutResponse postPutResponse = new PostPutResponse(true,
                ResponseError.ALREADY_REGISTERED.toString());
        ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
        PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(postPutResponse));
        PowerMockito.when(response.errorBody()).thenReturn(errorBody);

        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnAlreadyRegistered();
    }

    @Test
    public void registerWithCorrectCredentials_OnErrorResponse_CallsRegisterErrorOnPresenter()
            throws IOException {
        // arrange
        // sets fake call with a different error response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(false);

        PostPutResponse postPutResponse = new PostPutResponse(true,
                "another error");
        ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
        PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(postPutResponse));
        PowerMockito.when(response.errorBody()).thenReturn(errorBody);

        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnRegisterError();
    }

    @Test
    public void registerWithCorrectCredentials_OnFailure_CallsRegisterErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakePostPutResponseCall call = new FakePostPutResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnRegisterError();
    }
}
