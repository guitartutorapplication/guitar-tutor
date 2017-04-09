package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.models.interfaces.IAccountModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Testing AccountModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class, ResponseBody.class})
public class AccountModelTest {

    private IAccountModel model;
    private IAccountPresenter presenter;
    private int userId;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new AccountModel();

        presenter = Mockito.mock(IAccountPresenter.class);
        model.setPresenter(presenter);

        userId = 1;
        sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        model.setSharedPreferences(sharedPreferences);
    }

    @Test
    public void getAccountDetails_CallsGetAccountDetailsOnDatabaseApiWithIdFromSharedPreferences() {
        // arrange
        DatabaseApi api = Mockito.mock(DatabaseApi.class);
        Mockito.when(api.getAccountDetails(Mockito.anyInt()))
                .thenReturn(Mockito.mock(Call.class));
        ((AccountModel) model).setApi(api);

        // act
        model.getAccountDetails();

        // assert
        Mockito.verify(api).getAccountDetails(userId);
    }

    @Test
    public void getAccountDetails_OnSuccessfulResponse_CallsAccountDetailsRetrievedOnPresenter() {
        // arrange
        User user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        Response<User> response = FakeResponseCreator.getUserResponse(true, user);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));
        ((AccountModel) model).setApi(api);

        // act
        model.getAccountDetails();

        // assert
        Mockito.verify(presenter).modelOnAccountDetailsRetrieved(user);
    }

    @Test
    public void getAccountDetails_OnUnsuccessfulResponse_CallsErrorOnPresenter() {
        // arrange
        Response<User> response = FakeResponseCreator.getUserResponse(false, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(response));
        ((AccountModel) model).setApi(api);

        // act
        model.getAccountDetails();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getAccountDetails_OnFailure_CallsErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(null));
        ((AccountModel) model).setApi(api);

        // act
        model.getAccountDetails();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void logOut_RemovesUserIdFromSharedPreferences() {
        // arrange
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);

        // act
        model.logout();

        // assert
        Mockito.verify(editor).remove("user_id");
        Mockito.verify(editor).apply();
    }
}
