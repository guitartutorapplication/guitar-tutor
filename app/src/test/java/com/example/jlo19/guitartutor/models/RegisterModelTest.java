package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.ValidationResult;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeResponseWithMessageCall;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.models.retrofit.responses.ResponseWithMessage;
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
    public void registerWithCorrectCredentials_OnSuccessfulResponse_CallsRegisterSuccessOnPresenter()
            throws IOException {
        // arrange
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(true, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
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
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(false,
                ValidationResult.INVALID_EMAIL.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.INVALID_EMAIL);
    }

    @Test
    public void registerWithCorrectCredentials_OnEmailAlreadyRegisteredResponse_CallsAlreadyRegisteredOnPresenter()
            throws IOException {
        // arrange
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(false,
                ValidationResult.EMAIL_ALREADY_REGISTERED.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.EMAIL_ALREADY_REGISTERED);
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordTooShortResponse_CallsPasswordTooShortOnPresenter()
            throws IOException {
        // arrange
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(false,
                ValidationResult.PASSWORD_TOO_SHORT.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_TOO_SHORT);
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordNoUpperResponse_CallsPasswordNoUpperOnPresenter()
            throws IOException {
        // arrange
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(false,
                ValidationResult.PASSWORD_NO_UPPER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_UPPER);
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordNoLowerResponse_CallsPasswordNoLowerOnPresenter()
            throws IOException {
        // arrange
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(false,
                ValidationResult.PASSWORD_NO_LOWER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_LOWER);
    }

    @Test
    public void registerWithCorrectCredentials_OnPasswordNoNumberResponse_CallsPasswordNoNumberOnPresenter()
            throws IOException {
        // arrange
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(false,
                ValidationResult.PASSWORD_NO_NUMBER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnValidationFailed(ValidationResult.PASSWORD_NO_NUMBER);
    }


    @Test
    public void registerWithCorrectCredentials_OnErrorResponse_CallsRegisterErrorOnPresenter()
            throws IOException {
        // arrange
        Response<ResponseWithMessage> response = FakeResponseCreator.getResponseWithMessage(false,
                "another error");
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(response));
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
        DatabaseApi api = new FakeDatabaseApi(new FakeResponseWithMessageCall(null));
        ((RegisterModel) model).setApi(api);

        // act
        model.register("Kate", "kate@gmail.com", "kate@gmail.com", "Password123", "Password123");

        // assert
        Mockito.verify(presenter).modelOnRegisterError();
    }
}
