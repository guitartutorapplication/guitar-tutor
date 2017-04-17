package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeChordsCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.models.interfaces.IAccountActivityModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Testing AccountActivityModelTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class AccountActivityModelTest {

    private IAccountActivityModel model;
    private IAccountActivityPresenter presenter;
    private int userId;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new AccountActivityModel();

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        model.setSharedPreferences(sharedPreferences);

        presenter = PowerMockito.mock(IAccountActivityPresenter.class);
        model.setPresenter(presenter);
    }

    @Test
    public void getAccountActivity_CallsUserChordsOnApiWithIdFromSharedPreferences() {
        // arrange
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));
        ((AccountActivityModel) model).setApi(api);

        // act
        model.getAccountActivity();

        // assert
        Mockito.verify(api).getUserChords(userId);
    }

    @Test
    public void getAccountActivity_OnSuccessfulResponse_CallsAccountActivityRetrievedOnPresenter() {
        // arrange
        ArrayList<Chord> chords = new ArrayList<Chord>() {{
            add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
            add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            add(new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1));
        }};

        Response<List<Chord>> response = FakeResponseCreator.getChordsResponse(true, chords);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(response)));
        ((AccountActivityModel) model).setApi(api);

        // act
        model.getAccountActivity();

        // assert
        Mockito.verify(presenter).modelOnAccountActivityRetrieved(chords);
    }

    @Test
    public void getAccountActivity_OnUnsuccessfulResponse_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> response = FakeResponseCreator.getChordsResponse(false, null);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(response)));
        ((AccountActivityModel) model).setApi(api);

        // act
        model.getAccountActivity();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getAccountActivity_OnFailure_CallsErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeChordsCall(null)));
        ((AccountActivityModel) model).setApi(api);

        // act
        model.getAccountActivity();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

}
