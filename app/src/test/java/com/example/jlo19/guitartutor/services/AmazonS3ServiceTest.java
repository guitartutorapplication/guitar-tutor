package com.example.jlo19.guitartutor.services;

import android.graphics.Bitmap;
import android.os.Build;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.tasks.DownloadImageTask;
import com.example.jlo19.guitartutor.tasks.DownloadVideoTask;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
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
    private AmazonS3ServiceListener listener;
    private DownloadImageTask downloadImageTask;
    private DownloadVideoTask downloadVideoTask;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // stops real injection of tasks
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        service = new AmazonS3Service();

        listener = Mockito.mock(AmazonS3ServiceListener.class);
        service.setListener(listener);

        downloadImageTask = Mockito.mock(DownloadImageTask.class);
        ((AmazonS3Service) service).setDownloadImageTask(downloadImageTask);

        downloadVideoTask = Mockito.mock(DownloadVideoTask.class);
        ((AmazonS3Service) service).setDownloadVideoTask(downloadVideoTask);
    }

    @Test
    public void setDownloadImageTask_SetsListenerOnDownloadImageTask() {
        // assert
        Mockito.verify(downloadImageTask).setListener(service);
    }

    @Test
    public void setDownloadVideoTask_SetsListenerOnDownloadVideoTask() {
        // assert
        Mockito.verify(downloadVideoTask).setListener(service);
    }

    @Test
    public void getImage_SetsFilenameOnDownloadImageTask() {
        // act
        String expectedFilename = "filename";
        service.getImage(expectedFilename);

        // assert
        Mockito.verify(downloadImageTask).setFilename(expectedFilename);
    }

    @Test
    public void getVideo_SetsFilenameOnDownloadVideoTask() {
        // act
        String expectedFilename = "filename";
        service.getVideo(expectedFilename);

        // assert
        Mockito.verify(downloadVideoTask).setFilename(expectedFilename);
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
        service.getVideo("filename");

        // assert
        Mockito.verify(downloadVideoTask).execute();
    }

    @Test
    public void onDownloadFailed_CallsOnDownloadFailedOnListener() {
        // act
        service.onDownloadFailed();

        // assert
        Mockito.verify(listener).onDownloadFailed();
    }

    @Test
    public void onDownloadSuccessUrl_CallsOnDownloadSuccessUrlOnListener() {
        // act
        String expectedUrl = "url";
        service.onDownloadSuccess(expectedUrl);

        // assert
        Mockito.verify(listener).onDownloadSuccess(expectedUrl);
    }

    @Test
    public void onDownloadSuccessBitmap_CallsOnDownloadSuccessBitmapOnListener() {
        // act
        Bitmap expectedImage = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        service.onDownloadSuccess(expectedImage);

        // assert
        Mockito.verify(listener).onDownloadSuccess(expectedImage);
    }

    @Test
    public void setClient_IdentityPoolIdOfCredentialsProviderMatchesConfigResource() {
        // act
        service.setClient(getApp().getApplicationContext());

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.identity_pool_id),
                ((AmazonS3Service) service).credentialsProvider.getIdentityPoolId());
    }

    @Test
    public void setClient_ClientRegionIsEuWest1() {
        // act
        service.setClient(getApp().getApplicationContext());

         // assert
        Assert.assertEquals(Region.getRegion(Regions.EU_WEST_1),
                (((AmazonS3Service) service).client.getRegion()).toAWSRegion());
    }

    @Test
    public void setClient_SetsClientOnDownloadImageTask() {
        // act
        service.setClient(getApp().getApplicationContext());

        // assert
        Mockito.verify(downloadImageTask).setClient(((AmazonS3Service) service).client);
    }

    @Test
    public void setClient_SetsClientOnDownloadVideoTask() {
        // act
        service.setClient(getApp().getApplicationContext());

        // assert
        Mockito.verify(downloadVideoTask).setClient(((AmazonS3Service) service).client);
    }
}
