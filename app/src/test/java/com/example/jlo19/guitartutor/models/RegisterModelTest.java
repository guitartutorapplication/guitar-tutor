package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeMessageCall;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
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
    public void registerWithEmptyNameField_CallsFieldEmptyNameOnPresenter() {
        // act
        model.register("", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.FIELD_EMPTY_NAME));
    }

    @Test
    public void registerWithEmptyEmailField_CallsFieldEmptyEmailOnPresenter() {
        // act
        model.register("Kate", "", "kate@gmail.com", "Password123", "Password123");

        // assert
        List<ValidationError> errors = new ArrayList<ValidationError>() {{
            add(ValidationError.INVALID_EMAIL);
            add(ValidationError.EMAIL_MISMATCH);
            add(ValidationError.FIELD_EMPTY_EMAIL);
        }};
        Mockito.verify(presenter).modelOnValidationFailed(errors);
    }

    @Test
    public void registerWithEmptyConfirmEmailField_CallsFieldEmptyConfirmEmailOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "", "Password123", "Password123");

        // assert
        List<ValidationError> errors = new ArrayList<ValidationError>() {{
            add(ValidationError.EMAIL_MISMATCH);
            add(ValidationError.FIELD_EMPTY_CONFIRM_EMAIL);
        }};
        Mockito.verify(presenter).modelOnValidationFailed(errors);
    }

    @Test
    public void registerWithEmptyPasswordField_CallsFieldEmptyPasswordOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "", "Password123");

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
    public void registerWithEmptyConfirmPasswordField_CallsFieldEmptyPasswordOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "");

        // assert
        List<ValidationError> errors = new ArrayList<ValidationError>() {{
            add(ValidationError.PASSWORD_MISMATCH);
            add(ValidationError.FIELD_EMPTY_CONFIRM_PASSWORD);
        }};
        Mockito.verify(presenter).modelOnValidationFailed(errors);
    }

    @Test
    public void registerWithTwoDifferentEmails_CallsEmailMismatchOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "katet@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.EMAIL_MISMATCH));
    }

    @Test
    public void registerWithTwoDifferentEmails_CallsPasswordMismatchOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Passwrod123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_MISMATCH));
    }

    @Test
    public void registerWithInvalidEmail_CallsInvalidEmailOnPresenter() {
        // act
        model.register("Kate", "kate@gmailcom", "kate@gmailcom", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.INVALID_EMAIL));
    }

    @Test
    public void registerWithPasswordLessThanEightCharacters_CallsPasswordTooShortOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Pass1", "Pass1");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_TOO_SHORT));
    }

    @Test
    public void registerWithPasswordWithNoUpperCaseLetter_CallsPasswordNoUpperCaseLetterOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "password123", "password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_UPPER));
    }

    @Test
    public void registerWithPasswordWithNoLowerCaseLetter_CallsPasswordNoLowerCaseLetterOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "PASSWORD123", "PASSWORD123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_LOWER));
    }

    @Test
    public void registerWithPasswordWithNoNumber_CallsPasswordNoNumberOnPresenter() {
        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password", "Password");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_NUMBER));
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
    public void registerWithCorrectCredentials_OnSuccessfulResponse_CallsRegisterSuccessOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(true, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
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
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.INVALID_EMAIL.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.INVALID_EMAIL));
    }

    @Test
    public void registerWithCorrectCredentials_OnEmailAlreadyRegisteredResponse_CallsAlreadyRegisteredOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.EMAIL_ALREADY_REGISTERED.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.EMAIL_ALREADY_REGISTERED));
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordTooShortResponse_CallsPasswordTooShortOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_TOO_SHORT.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_TOO_SHORT));
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordNoUpperResponse_CallsPasswordNoUpperOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_UPPER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_UPPER));
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordNoLowerResponse_CallsPasswordNoLowerOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_LOWER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_LOWER));
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordNoNumberResponse_CallsPasswordNoNumberOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_NUMBER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_NUMBER));
    }


    @Test
    public void registerWithCorrectCredentials_OnErrorResponse_CallsRegisterErrorOnPresenter()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                "another error");
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnRegisterError();
    }

    @Test
    public void registerWithCorrectCredentials_OnFailure_CallsRegisterErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(null));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnRegisterError();
    }
}
