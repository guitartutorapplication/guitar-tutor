package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeMessageCall;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.listeners.RegisterListener;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterInteractor;
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
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Testing RegisterInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResponseBody.class, Response.class})
public class RegisterInteractorTest {

    private RegisterListener listener;

    @Before
    public void setUp() {
        listener = PowerMockito.mock(IRegisterPresenter.class);
    }

    @Test
    public void register_CallsRegisterOnDatabaseApi() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.registerUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mockito.mock(Call.class));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        registerInteractor.register(expectedName, expectedEmail, expectedPassword);

        // assert
        Mockito.verify(api).registerUser(expectedName, expectedEmail, expectedPassword);
    }

    @Test
    public void register_OnSuccessfulResponse_CallsRegisterSuccessOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(true, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onRegisterSuccess();
    }

    @Test
    public void register_OnInvalidEmailResponse_CallsInvalidEmailOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.INVALID_EMAIL.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.INVALID_EMAIL));
    }

    @Test
    public void register_OnEmailAlreadyRegisteredResponse_CallsAlreadyRegisteredOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.EMAIL_ALREADY_REGISTERED.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.EMAIL_ALREADY_REGISTERED));
    }

    @Test
    public void register_OnPasswordTooShortResponse_CallsPasswordTooShortOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_TOO_SHORT.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_TOO_SHORT));
    }

    @Test
    public void register_OnPasswordNoUpperResponse_CallsPasswordNoUpperOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_UPPER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_UPPER));
    }

    @Test
    public void register_OnPasswordNoLowerResponse_CallsPasswordNoLowerOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_LOWER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_LOWER));
    }

    @Test
    public void register_OnPasswordNoNumberResponse_CallsPasswordNoNumberOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_NUMBER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_NUMBER));
    }


    @Test
    public void register_OnErrorResponse_CallsRegisterErrorOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                "another error");
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onRegisterError();
    }

    @Test
    public void register_OnFailure_CallsRegisterErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(null));
        IRegisterInteractor registerInteractor = new RegisterInteractor(api);
        registerInteractor.setListener(listener);

        // act
        registerInteractor.register("Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onRegisterError();
    }
}
