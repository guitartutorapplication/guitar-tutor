package com.example.jlo19.guitartutor.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.jlo19.guitartutor.listeners.DownloadImageTaskListener;

import java.io.IOException;

/**
 * Asynchronous task to retrieve image from Amazon S3
 */
public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

    private AmazonS3Client client;
    private String filename;
    private DownloadImageTaskListener listener;

    public DownloadImageTask(AmazonS3Client client, String filename, DownloadImageTaskListener listener) {
        this.client = client;
        this.filename = filename;
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        S3ObjectInputStream content = client.getObject("guitar.tutor.data", filename)
                .getObjectContent();

        // retrieves bitmap from input stream
        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(content);
        } catch (IOException e) {
            // if exception is found, cancel task
            cancel(true);
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        listener.onDownloadSuccess(bitmap);
    }

    @Override
    protected void onCancelled() {
        listener.onDownloadFailed();
    }

}
