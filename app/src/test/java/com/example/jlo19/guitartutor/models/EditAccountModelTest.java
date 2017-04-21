package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeMessageCall;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountModel;
import com.example.jlo19.guitartutor.presenters.EditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private String apiKey;

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
        apiKey = "api_key";
        Mockito.when(sharedPreferences.getString("api_key", "")).thenReturn(apiKey);
        model.setSharedPreferences(sharedPreferences);
    }

    @Test
    public void saveWithEmptyNameField_CallsFieldEmptyNameOnPresenter() {
        // act
        model.save("", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.FIELD_EMPTY_NAME));
    }

    @Test
    public void saveWithEmptyEmailField_CallsFieldEmptyEmailOnPresenter() {
        // act
        model.save("Kate", "", "kate@gmail.com", "Password123", "Password123");

        // assert
        List<ValidationError> errors = new ArrayList<ValidationError>() {{
            add(ValidationError.INVALID_EMAIL);
            add(ValidationError.EMAIL_MISMATCH);
            add(ValidationError.FIELD_EMPTY_EMAIL);
        }};
        Mockito.verify(presenter).modelOnValidationFailed(errors);
    }

    @Test
    public void saveWithEmptyConfirmEmailField_CallsFieldEmptyConfirmEmailOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "", "Password123", "Password123");

        // assert
        List<ValidationError> errors = new ArrayList<ValidationError>() {{
            add(ValidationError.EMAIL_MISMATCH);
            add(ValidationError.FIELD_EMPTY_CONFIRM_EMAIL);
        }};
        Mockito.verify(presenter).modelOnValidationFailed(errors);
    }

    @Test
    public void saveWithEmptyPasswordField_CallsFieldEmptyPasswordOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "", "Password123");

        // assert
        List<ValidationError> errors = new ArrayList<ValidationError>() {{
            add(ValidationError.PASSWORD_NO_NUMBER);
            add(ValidationError.PASSWORD_NO_LOWER);
            add(ValidationError.PASSWORD_NO_UPPER);
            add(ValidationError.PASSWORD_TOO_SHORT);
            add(ValidationError.PASSWORD_MISMATCH);
            add(ValidationError.FIELD_EMPTY_PASSWORD);
        }};
        Mockito.verify(presenter).modelOnValidationFailed(errors);
    }

    @Test
    public void saveWithEmptyConfirmPasswordField_CallsEmptyFieldConfirmPasswordOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "");

        // assert
        List<ValidationError> errors = new ArrayList<ValidationError>() {{
            add(ValidationError.PASSWORD_MISMATCH);
            add(ValidationError.FIELD_EMPTY_CONFIRM_PASSWORD);
        }};
        Mockito.verify(presenter).modelOnValidationFailed(errors);
    }

    @Test
    public void saveWithTwoDifferentEmails_CallsEmailMismatchOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "katet@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.EMAIL_MISMATCH));
    }

    @Test
    public void saveWithTwoDifferentEmails_CallsPasswordMismatchOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Passwrod123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_MISMATCH));
    }

    @Test
    public void saveWithInvalidEmail_CallsInvalidEmailOnPresenter() {
        // act
        model.save("Kate", "kate@gmailcom", "kate@gmailcom", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.INVALID_EMAIL));
    }

    @Test
    public void saveWithPasswordLessThanEightCharacters_CallsPasswordTooShortOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Pass1", "Pass1");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_TOO_SHORT));
    }

    @Test
    public void saveWithPasswordWithNoUpperCaseLetter_CallsPasswordNoUpperCaseLetterOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "password123", "password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_UPPER));
    }

    @Test
    public void saveWithPasswordWithNoLowerCaseLetter_CallsPasswordNoLowerCaseLetterOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "PASSWORD123", "PASSWORD123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_LOWER));
    }

    @Test
    public void saveWithPasswordWithNoNumber_CallsPasswordNoNumberOnPresenter() {
        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password", "Password");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_NUMBER));
    }

    @Test
    public void saveWithCorrectCredentials_CallsEditAccountDetailsOnDatabaseApiWithCredentials() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.editAccountDetails(Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mockito.mock(Call.class));
        ((EditAccountModel) model).setApi(api);

        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        model.save(expectedName, expectedEmail, expectedEmail, expectedPassword, expectedPassword);

        // assert
        Mockito.verify(api).editAccountDetails(apiKey, userId, expectedName, expectedEmail, expectedPassword);
    }

    @Test
    public void saveWithCorrectCredentials_OnSuccessfulResponse_CallsSaveSuccessOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(true, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
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
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.INVALID_EMAIL.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.INVALID_EMAIL));
    }

    @Test
    public void saveWithCorrectCredentials_OnEmailAlreadyRegisteredResponse_CallsAlreadyRegisteredOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.EMAIL_ALREADY_REGISTERED.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.EMAIL_ALREADY_REGISTERED));
    }

    @Test
    public void saveWithCorrectCredentials_OnPasswordTooShortResponse_CallsPasswordTooShortOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_TOO_SHORT.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_TOO_SHORT));
    }

    @Test
    public void saveWithCorrectCredentials_OnPasswordNoUpperResponse_CallsPasswordNoUpperOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_UPPER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_UPPER));
    }

    @Test
    public void saveWithCorrectCredentials_OnPasswordNoLowerResponse_CallsPasswordNoLowerOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_LOWER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_LOWER));
    }

    @Test
    public void saveWithCorrectCredentials_OnPasswordNoNumberResponse_CallsPasswordNoNumberOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_NUMBER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_NUMBER));
    }

    @Test
    public void saveWithCorrectCredentials_OnErrorResponse_CallsSaveErrorOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                "another error");
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnSaveError();
    }

    @Test
    public void saveWithCorrectCredentials_OnFailure_CallsSaveErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(null));
        ((EditAccountModel) model).setApi(api);

        // act
        model.save("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnSaveError();
    }
}
