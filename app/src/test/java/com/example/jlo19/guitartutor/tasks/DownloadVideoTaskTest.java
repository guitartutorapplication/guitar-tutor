package com.example.jlo19.guitartutor.tasks;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.jlo19.guitartutor.listeners.DownloadVideoTaskListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Testing DownloadVideoTask
 */

public class DownloadVideoTaskTest {

    private DownloadVideoTask task;
    private DownloadVideoTaskListener listener;
    private String filename;
    private AmazonS3 client;
    private URL url;

    @Before
    public void setUp() throws MalformedURLException {
        listener = Mockito.mock(DownloadVideoTaskListener.class);
        filename = "filename";

        client = Mockito.mock(AmazonS3.class);
        url = new URL("http://url.com");
        Mockito.when(client.generatePresignedUrl((GeneratePresignedUrlRequest) Mockito.any()))
                .thenReturn(url);

        task = new DownloadVideoTask(client, filename, listener);
    }

    @Test
    public void doInBackground_CallsGeneratePresignedUrlOnClient() {
        // act
        task.doInBackground();

        // assert
        Mockito.verify(client).generatePresignedUrl(task.request);
        Assert.assertEquals("guitar.tutor.data", task.request.getBucketName());
        Assert.assertEquals(filename, task.request.getKey());
    }

    @Test
    public void doInBackground_ReturnsStringUrl() {
        // act
        String actualUrl = task.doInBackground();

        // assert
        Assert.assertEquals(url.toString(), actualUrl);
    }

    @Test
    public void onPostExecute_CallsDownloadSuccessOnListener() {
        // act
        task.onPostExecute(url.toString());

        // assert
        Mockito.verify(listener).onVideoDownloadSuccess(url.toString());
    }

    @Test
    public void onPostExecuteWithNull_CallsDownloadFailedOnListener() {
        // act
        task.onPostExecute(null);

        // assert
        Mockito.verify(listener).onVideoDownloadFailed();
    }
}
