package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.listeners.GetAccountDetailsListener;
import com.example.jlo19.guitartutor.models.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Testing GetAccountDetailsInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class, ResponseBody.class})
public class GetAccountDetailsInteractorTest {

    private GetAccountDetailsListener listener;

    @Before
    public void setUp() {
        listener = Mockito.mock(GetAccountDetailsListener.class);
    }

    @Test
    public void getAccountDetails_CallsGetAccountDetailsOnDatabaseApi() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.getAccountDetails(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(Mockito.mock(Call.class));

        IGetAccountDetailsInteractor getAccountDetailsInteractor = new GetAccountDetailsInteractor(api);
        getAccountDetailsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        int userId = 2;
        getAccountDetailsInteractor.getAccountDetails(apiKey, userId);

        // assert
        Mockito.verify(api).getAccountDetails(apiKey, userId);
    }

    @Test
    public void getAccountDetails_OnSuccessfulResponse_CallsAccountDetailsRetrievedOnListener() {
        // arrange
        User user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));

        IGetAccountDetailsInteractor getAccountDetailsInteractor = new GetAccountDetailsInteractor(api);
        getAccountDetailsInteractor.setListener(listener);

        // act
        getAccountDetailsInteractor.getAccountDetails("api_key", 2);

        // assert
        Mockito.verify(listener).onAccountDetailsRetrieved(user);
    }

    @Test
    public void getAccountDetails_OnUnsuccessfulResponse_CallsErrorOnListener() {
        // arrange
        Response<User> response = FakeResponseCreator.getUserResponse(false, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));

        IGetAccountDetailsInteractor getAccountDetailsInteractor = new GetAccountDetailsInteractor(api);
        getAccountDetailsInteractor.setListener(listener);

        // act
        getAccountDetailsInteractor.getAccountDetails("api_key", 2);

        // assert
        Mockito.verify(listener).onGetAccountDetailsError();
    }

    @Test
    public void getAccountDetails_OnFailure_CallsErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(null));
        IGetAccountDetailsInteractor getAccountDetailsInteractor = new GetAccountDetailsInteractor(api);
        getAccountDetailsInteractor.setListener(listener);

        // act
        getAccountDetailsInteractor.getAccountDetails("api_key", 2);

        // assert
        Mockito.verify(listener).onGetAccountDetailsError();
    }
}
