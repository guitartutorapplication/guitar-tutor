package com.example.jlo19.guitartutor.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringInputStream;
import com.example.jlo19.guitartutor.listeners.DownloadImageTaskListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Testing DownloadImageTask
 */
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
        Bitmap expectedBitmap = BitmapFactory.decodeByteArray(IOUtils.toByteArray(input), 0,
                IOUtils.toByteArray(input).length);
        task.onPostExecute(expectedBitmap);

        // assert
        Mockito.verify(listener).onDownloadSuccess(expectedBitmap);
    }

    @Test
    public void onCancelled_CallsOnDownloadFailedOnListener() {
        // act
        task.onCancelled();

        // assert
        Mockito.verify(listener).onDownloadFailed();
    }
}
