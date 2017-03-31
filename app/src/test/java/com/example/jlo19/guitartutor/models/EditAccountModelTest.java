package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ResponseError;
import com.example.jlo19.guitartutor.enums.ValidationResult;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakePostPutResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountModel;
import com.example.jlo19.guitartutor.models.retrofit.PostPutResponse;
import com.example.jlo19.guitartutor.presenters.EditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
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
 * Testing EditAccountModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, ResponseBody.class, Response.class})
public class EditAccountModelTest {

    private IEditAccountModel model;
    private IEditAccountPresenter presenter;
    private int userId;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new EditAccountModel();

        presenter = PowerMockito.mock(EditAccountPresenter.class);
        model.setPresenter(presenter);

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        model.setSharedPreferences(sharedPreferences);
    }

    @Test
    public void saveWithEmptyNameField_CallsEmptyFieldOnPresenter() {
        // act
        model.save("", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void saveWithEmptyEmailField_CallsEmptyFieldOnPresenter() {
        // act
        model.save("Kate", "", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void saveWithEmptyConfirmEmailField_CallsEmptyFieldOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void saveWithEmptyPasswordField_CallsEmptyFieldOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void saveWithEmptyConfirmPasswordField_CallsEmptyFieldOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.FIELD_EMPTY);
    }

    @Test
    public void saveWithTwoDifferentEmails_CallsEmailMismatchOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "katet@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.EMAIL_MISMATCH);
    }

    @Test
    public void saveWithTwoDifferentEmails_CallsPasswordMismatchOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Passwrod123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_MISMATCH);
    }

    @Test
    public void saveWithInvalidEmail_CallsInvalidEmailOnPresenter() {
        // act
        model.save("Kate", "kate@gmailcom", "kate@gmailcom", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.INVALID_EMAIL);
    }

    @Test
    public void saveWithPasswordLessThanEightCharacters_CallsPasswordTooShortOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "pass", "pass");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_TOO_SHORT);
    }

    @Test
    public void saveWithPasswordWithNoUpperCaseLetter_CallsPasswordNoUpperCaseLetterOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "password123", "password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_UPPER);
    }

    @Test
    public void saveWithPasswordWithNoLowerCaseLetter_CallsPasswordNoLowerCaseLetterOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "PASSWORD123", "PASSWORD123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_LOWER);
    }

    @Test
    public void saveWithPasswordWithNoNumber_CallsPasswordNoNumberOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password", "Password");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_NUMBER);
    }

    @Test
    public void saveWithCorrectCredentials_CallsEditAccountDetailsOnDatabaseApiWithCredentials() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.editAccountDetails(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mockito.mock(Call.class));
        ((EditAccountModel) model).setApi(api);

        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        model.save(expectedName, expectedEmail, expectedEmail, expectedPassword, expectedPassword);

        // assert
        Mockito.verify(api).editAccountDetails(userId, expectedName, expectedEmail, expectedPassword);
    }

    @Test
    public void saveWithCorrectCredentials_OnSuccessfulResponse_CallsSaveSuccessOnPresenter() {
        // arrange
        // sets fake call with a successful response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(true);

        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnSaveSuccess();
    }

    @Test
    public void saveWithCorrectCredentials_OnInvalidEmailResponse_CallsInvalidEmailOnPresenter()
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
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.INVALID_EMAIL);
    }

    @Test
    public void saveWithCorrectCredentials_OnErrorResponse_CallsSaveErrorOnPresenter()
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
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnSaveError();
    }

    @Test
    public void saveWithCorrectCredentials_OnFailure_CallsSaveErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakePostPutResponseCall call = new FakePostPutResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnSaveError();
    }
}