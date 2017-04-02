package com.example.jlo19.guitartutor.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakePostPutResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.models.retrofit.PostPutResponse;
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

import retrofit2.Response;

/**
 * Testing LearnChordModelTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class LearnChordModelTest {

    private ILearnChordModel model;
    private ILearnChordPresenter presenter;
    private IAmazonS3Service service;
    private int userId;

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
        model.setSharedPreferences(sharedPreferences);
    }

    @Test
    public void onImageDownloadFailed_CallsOnDownloadFailedOnPresenter() {
        // act
        model.onImageDownloadFailed();

        // assert
        Mockito.verify(presenter).modelOnImageDownloadFailed();
    }

    @Test
    public void onVideoDownloadFailed_CallsOnDownloadFailedOnPresenter() {
        // act
        model.onVideoDownloadFailed();

        // assert
        Mockito.verify(presenter).modelOnVideoDownloadFailed();
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
    public void onVideoDownloadSuccess_CallsOnDownloadSuccessWithUrlOnPresenter() {
        // act
        String expectedUrl = "url";
        model.onVideoDownloadSuccess(expectedUrl);

        // assert
        Mockito.verify(presenter).modelOnVideoDownloadSuccess(expectedUrl);
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
        Mockito.verify(service).getVideo(expectedFilename);
    }

    @Test
    public void addLearntChord_CallsAddLearntChordOnApiWithIdFromSharedPreferences() {
        // arrange
        int chordId = 3;
        // sets fake call with a response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakePostPutResponseCall(response)));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(chordId);

        // assert
        Mockito.verify(api).addLearntChord(userId, chordId);
    }

    @Test
    public void addLearntChord_OnSuccessfulResponse_CallsLearntChordAddedOnPresenter() {
        // arrange
        // sets fake call with a successful response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        Mockito.when(response.isSuccessful()).thenReturn(true);

        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(response));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(3);

        // arrange
        Mockito.verify(presenter).modelOnLearntChordAdded();
    }

    @Test
    public void addLearntChord_OnErrorResponse_CallsLearntChordErrorOnPresenter() {
        // arrange
        // sets fake call with a error response
        Response<PostPutResponse> response = (Response<PostPutResponse>)
                PowerMockito.mock(Response.class);
        Mockito.when(response.isSuccessful()).thenReturn(false);

        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(response));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(3);

        // assert
        Mockito.verify(presenter).modelOnAddLearntChordError();
    }

    @Test
    public void addLearntChord_OnFailure_CallsLearntChordErrorOnPresenter() {
        // arrange
        DatabaseApi api = new FakeDatabaseApi(new FakePostPutResponseCall(null));
        ((LearnChordModel) model).setDatabaseApi(api);

        // act
        model.addLearntChord(3);

        // assert
        Mockito.verify(presenter).modelOnAddLearntChordError();

    }
}
