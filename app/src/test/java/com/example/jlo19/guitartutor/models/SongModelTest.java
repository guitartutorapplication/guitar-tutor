package com.example.jlo19.guitartutor.models;

import android.content.Context;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.ISongModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing SongModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class SongModelTest {

    private ISongModel model;
    private ISongPresenter presenter;
    private IAmazonS3Service service;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new SongModel();

        presenter = Mockito.mock(ISongPresenter.class);
        model.setPresenter(presenter);

        service = Mockito.mock(IAmazonS3Service.class);
        ((SongModel) model).createAmazonS3Service(service);
    }

    @Test
    public void onUrlDownloadFailed_CallsOnDownloadFailedOnPresenter() {
        // act
        model.onUrlDownloadFailed();

        // assert
        Mockito.verify(presenter).modelOnUrlDownloadFailed();
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
    public void getAudio_CallsGetUrlOnServiceWithFilename() {
        // act
        String expectedFilename = "filename";
        model.getAudio(expectedFilename);

        // assert
        Mockito.verify(service).getUrl(expectedFilename);
    }
}
