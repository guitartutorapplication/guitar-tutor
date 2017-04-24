package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeMessageCall;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.listeners.EditAccountDetailsListener;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.presenters.EditAccountPresenter;
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
 * Testing EditAccountDetailsInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResponseBody.class, Response.class})
public class EditAccountDetailsInteractorTest {

    private EditAccountDetailsListener listener;

    @Before
    public void setUp() {
        listener = PowerMockito.mock(EditAccountPresenter.class);
    }

    @Test
    public void save_CallsEditAccountDetailsOnDatabaseApiWithCredentials() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.editAccountDetails(Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mockito.mock(Call.class));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "Password123";
        String apiKey = "api_key";
        int userId = 2;
        editAccountDetailsInteractor.save(apiKey, userId, expectedName, expectedEmail,
                expectedPassword);

        // assert
        Mockito.verify(api).editAccountDetails(apiKey, userId, expectedName, expectedEmail,
                expectedPassword);
    }

    @Test
    public void save_OnSuccessfulResponse_CallsSaveSuccessOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(true, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onSaveSuccess();
    }

    @Test
    public void save_OnInvalidEmailResponse_CallsInvalidEmailOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.INVALID_EMAIL.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.INVALID_EMAIL));
    }

    @Test
    public void save_OnEmailAlreadyRegisteredResponse_CallsAlreadyRegisteredOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.EMAIL_ALREADY_REGISTERED.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "kate@gmail.com");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.EMAIL_ALREADY_REGISTERED));
    }

    @Test
    public void save_OnPasswordTooShortResponse_CallsPasswordTooShortOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_TOO_SHORT.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_TOO_SHORT));
    }

    @Test
    public void save_OnPasswordNoUpperResponse_CallsPasswordNoUpperOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_UPPER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_UPPER));
    }

    @Test
    public void save_OnPasswordNoLowerResponse_CallsPasswordNoLowerOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_LOWER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_LOWER));
    }

    @Test
    public void save_OnPasswordNoNumberResponse_CallsPasswordNoNumberOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                ValidationError.PASSWORD_NO_NUMBER.toString());
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onValidationFailed(Collections.singletonList(
                ValidationError.PASSWORD_NO_NUMBER));
    }

    @Test
    public void save_OnErrorResponse_CallsSaveErrorOnListener()
            throws IOException {
        // arrange
        Response<List<String>> response = FakeResponseCreator.getMessageResponse(false,
                "another error");
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(response));

        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onSaveError();
    }

    @Test
    public void save_OnFailure_CallsSaveErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeMessageCall(null));
        IEditAccountDetailsInteractor editAccountDetailsInteractor = new
                EditAccountDetailsInteractor(api);
        editAccountDetailsInteractor.setListener(listener);

        // act
        editAccountDetailsInteractor.save("api_key", 2, "Kate", "kate@gmail.com", "Password123");

        // assert
        Mockito.verify(listener).onSaveError();
    }
}
