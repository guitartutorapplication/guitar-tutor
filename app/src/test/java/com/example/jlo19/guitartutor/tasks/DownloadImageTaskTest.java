package com.example.jlo19.guitartutor.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringInputStream;
import com.example.jlo19.guitartutor.listeners.DownloadImageTaskListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Testing DownloadImageTask
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IOUtils.class)
public class DownloadImageTaskTest {

    private DownloadImageTask task;
    private AmazonS3 client;
    private DownloadImageTaskListener listener;
    private String filename;
    private S3Object object;
    private S3ObjectInputStream input;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        listener = Mockito.mock(DownloadImageTaskListener.class);
        filename = "filename";

        client = Mockito.mock(AmazonS3.class);
        object = Mockito.mock(S3Object.class);
        input = new S3ObjectInputStream(new StringInputStream("image"));

        Mockito.when(object.getObjectContent()).thenReturn(Mockito.mock(S3ObjectInputStream.class));
        Mockito.when(client.getObject("guitar.tutor.data", filename))
                .thenReturn(object);
        Mockito.when(object.getObjectContent()).thenReturn(input);

        task = new DownloadImageTask(client, filename, listener);
    }

    @Test
    public void doInBackground_CallsGetObjectOnClient() {
        // act
        task.doInBackground();

        // assert
        Mockito.verify(client).getObject("guitar.tutor.data", filename);
    }

    @Test
    public void doInBackground_CallsGetObjectContentOnObject() {
        // act
        task.doInBackground();

        // assert
        Mockito.verify(object).getObjectContent();
    }

    @Test
    public void doInBackground_ReturnsBitmap() throws IOException {
        // act
        Bitmap actualBitmap = task.doInBackground();

        // assert
        Bitmap expectedBitmap = BitmapFactory.decodeByteArray(IOUtils.toByteArray(input), 0,
                IOUtils.toByteArray(input).length);
        Assert.assertEquals(expectedBitmap, actualBitmap);
    }

    @Test
    public void onPostExecute_CallsOnDownloadSuccessOnListener() throws IOException {
        // act
        Bitmap expectedBitmap = Mockito.mock(Bitmap.class);
        task.onPostExecute(expectedBitmap);

        // assert
        Mockito.verify(listener).onImageDownloadSuccess(expectedBitmap);
    }

    @Test
    public void onPostExecuteWithNull_CallsOnDownloadFailedOnListener() {
        // act
        task.onPostExecute(null);

        // assert
        Mockito.verify(listener).onImageDownloadFailed();
    }

    @Test
    public void doInBackground_WhenAmazonClientExceptionThrown_ReturnsNull() {
        // arrange
        Mockito.when(client.getObject("guitar.tutor.data", filename)).thenThrow(AmazonClientException.class);

        // act
        Bitmap expectedBitmap = task.doInBackground();

        // assert
        Assert.assertNull(expectedBitmap);
    }

    @Test
    public void doInBackground_WhenIOExceptionThrown_ReturnsNull() throws IOException {
        // arrange
        PowerMockito.mockStatic(IOUtils.class);
        PowerMockito.when(IOUtils.toByteArray(input)).thenThrow(IOException.class);

        // act
        Bitmap expectedBitmap = task.doInBackground();

        // assert
        Assert.assertNull(expectedBitmap);
    }
}
