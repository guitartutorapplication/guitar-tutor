package com.example.jlo19.guitartutor.services;

import android.graphics.Bitmap;
import android.os.Build;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceImageListener;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceUrlListener;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.tasks.DownloadImageTask;
import com.example.jlo19.guitartutor.tasks.DownloadUrlTask;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Testing AmazonS3Service
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class AmazonS3ServiceTest {

    private IAmazonS3Service service;
    private AmazonS3ServiceImageListener imageListener;
    private DownloadImageTask downloadImageTask;
    private DownloadUrlTask downloadUrlTask;
    private AmazonS3ServiceUrlListener urlListener;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        service = Mockito.spy(new AmazonS3Service(getApp().getApplicationContext()));

        imageListener = Mockito.mock(AmazonS3ServiceImageListener.class);
        service.setImageListener(imageListener);

        urlListener = Mockito.mock(AmazonS3ServiceUrlListener.class);
        service.setUrlListener(urlListener);

        downloadImageTask = Mockito.mock(DownloadImageTask.class);
        Mockito.when(((AmazonS3Service) service).getDownloadImageTask("filename")).thenReturn(
                downloadImageTask);

        downloadUrlTask = Mockito.mock(DownloadUrlTask.class);
        Mockito.when(((AmazonS3Service) service).getDownloadUrlTask("filename")).thenReturn(
                downloadUrlTask);
    }

    @Test
    public void getImage_CallsExecuteOnDownloadImageTask() {
        // act
        service.getImage("filename");

        // assert
        Mockito.verify(downloadImageTask).execute();
    }

    @Test
    public void getVideo_CallsExecuteOnDownloadVideoTask() {
        // act
        service.getUrl("filename");

        // assert
        Mockito.verify(downloadUrlTask).execute();
    }

    @Test
    public void onImageDownloadFailed_CallsOnImageDownloadFailedOnListener() {
        // act
        service.onImageDownloadFailed();

        // assert
        Mockito.verify(imageListener).onImageDownloadFailed();
    }

    @Test
    public void onUrlDownloadSuccess_CallsOnUrlDownloadSuccessOnListener() {
        // act
        String expectedUrl = "url";
        service.onUrlDownloadSuccess(expectedUrl);

        // assert
        Mockito.verify(urlListener).onUrlDownloadSuccess(expectedUrl);
    }

    @Test
    public void onUrlDownloadFailed_CallsOnUrlDownloadFailedOnListener() {
        // act
        service.onUrlDownloadFailed();

        // assert
        Mockito.verify(urlListener).onUrlDownloadFailed();
    }

    @Test
    public void onImageDownloadSuccessBitmap_CallsOnImageDownloadSuccessOnListener() {
        // act
        Bitmap expectedImage = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        service.onImageDownloadSuccess(expectedImage);

        // assert
        Mockito.verify(imageListener).onImageDownloadSuccess(expectedImage);
    }

    @Test
    public void identityPoolIdOfCredentialsProviderMatchesConfigResource() {
        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.identity_pool_id),
                ((AmazonS3Service) service).credentialsProvider.getIdentityPoolId());
    }

    @Test
    public void clientRegionIsEuWest1() {
         // assert
        Assert.assertEquals(Region.getRegion(Regions.EU_WEST_1),
                (((AmazonS3Service) service).client.getRegion()).toAWSRegion());
    }
}
