package com.example.jlo19.guitartutor.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeUserCall;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Testing LearnChordModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class, ResponseBody.class})
public class LearnChordModelTest {

    private ILearnChordModel model;
    private ILearnChordPresenter presenter;
    private IAmazonS3Service service;
    private int userId;
    private User user;
    private String apiKey;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new LearnChordModel();

        presenter = Mockito.mock(ILearnChordPresenter.class);
        model.setPresenter(presenter);

        service = Mockito.mock(IAmazonS3Service.class);
        ((LearnChordModel) model).createAmazonS3Service(service);

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        apiKey = "api_key";
        Mockito.when(sharedPreferences.getString("api_key", "")).thenReturn(apiKey);
        model.setSharedPreferences(sharedPreferences);

        user = new User(userId, "Kate", "katesmith@gmail.com", 2, 2000, "api_key");
    }

    @Test
    public void onImageDownloadFailed_CallsOnDownloadFailedOnPresenter() {
        // act
        model.onImageDownloadFailed();

        // assert
        Mockito.verify(presenter).modelOnImageDownloadFailed();
    }

    @Test
    public void onUrlDownloadFailed_CallsOnDownloadFailedOnPresenter() {
        // act
        model.onUrlDownloadFailed();

        // assert
        Mockito.verify(presenter).modelOnUrlDownloadFailed();
    }

    @Test
    public void onImageDownloadSuccess_CallsOnDownloadSuccessWithBitmapOnPresenter() {
        // act
        Bitmap expectedBitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        model.onImageDownloadSuccess(expectedBitmap);

        // assert
        Mockito.verify(presenter).modelOnImageDownloadSuccess(expectedBitmap);
    }

    @Test
    public void onUrlDownloadSuccess_CallsOnDownloadSuccessWithUrlOnPresenter() {
        // act
        String expectedUrl = "url";
        model.onUrlDownloadSuccess(expectedUrl);

        // assert
        Mockito.verify(presenter).modelOnUrlDownloadSuccess(expectedUrl);
    }

    @Test
    public void setContext_CallsSetClientOnServiceWithContext() {
        // act
        Context expectedContext = Mockito.mock(Context.class);
        model.setContext(expectedContext);

        // assert
        Mockito.verify(service).setClient(expectedContext);
    }

    @Test
    public void getImage_CallsGetImageOnServiceWithFilename() {
        // act
        String expectedFilename = "filename";
        model.getImage(expectedFilename);

        // assert
        Mockito.verify(service).getImage(expectedFilename);
    }

    @Test
    public void getVideo_CallsGetVideoOnServiceWithFilename() {
        // act
        String expectedFilename = "filename";
        model.getVideo(expectedFilename);

        // assert
        Mockito.verify(service).getUrl(expectedFilename);
    }

    @Test
    public void addLearntChord_CallsAddLearntChordOnApiWithIdAndApiKeyFromSharedPreferences() {
        // arrange
        int chordId = 3;
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeUserCall(null)));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(chordId);

        // assert
        Mockito.verify(api).addUserChord(apiKey, userId, chordId);
    }

    @Test
    public void addLearntChord_OnSuccessfulResponse_CallsLearntChordAddedOnPresenter() {
        // arrange
        Response<User> userResponse = FakeResponseCreator.getUserResponse(true, user);

        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(userResponse));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(3);

        // arrange
        Mockito.verify(presenter).modelOnLearntChordAdded(user.getLevel(), user.getAchievements());
    }

    @Test
    public void addLearntChord_OnUnsuccessfulResponse_CallsLearntChordErrorOnPresenter() {
        // arrange
        Response<User> userResponse = FakeResponseCreator.getUserResponse(false, null);

        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(userResponse));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(3);

        // assert
        Mockito.verify(presenter).modelOnAddLearntChordError();
    }

    @Test
    public void addLearntChord_OnFailure_CallsLearntChordErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeUserCall(null));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(3);

        // assert
        Mockito.verify(presenter).modelOnAddLearntChordError();

    }
}
