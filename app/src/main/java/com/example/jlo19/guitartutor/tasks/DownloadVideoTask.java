package com.example.jlo19.guitartutor.tasks;

import android.os.AsyncTask;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.jlo19.guitartutor.listeners.DownloadVideoTaskListener;

import java.net.URL;

/**
 * Asynchronous task to retrieve video from Amazon S3
 */

public class DownloadVideoTask extends AsyncTask<Void, Void, URL> {

    private DownloadVideoTaskListener listener;
    private String filename;
    private AmazonS3Client client;

    public DownloadVideoTask(AmazonS3Client client, String filename, DownloadVideoTaskListener listener) {
        this.client = client;
        this.filename = filename;
        this.listener = listener;
    }

    @Override
    protected URL doInBackground(Void... params) {
        // retrieves URL from S3
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest("guitar.tutor.data",
                filename);

        return client.generatePresignedUrl(request);
    }

    @Override
    protected void onPostExecute(URL url) {
        listener.onDownloadSuccess(url);
    }
}
