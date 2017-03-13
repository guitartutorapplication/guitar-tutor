package com.example.jlo19.guitartutor.tasks;

import android.os.AsyncTask;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.jlo19.guitartutor.listeners.DownloadVideoTaskListener;

import java.net.URL;

/**
 * Asynchronous task to retrieve video from Amazon S3
 */

public class DownloadVideoTask extends AsyncTask<Void, Void, String> {

    private DownloadVideoTaskListener listener;
    private String filename;
    private AmazonS3 client;
    GeneratePresignedUrlRequest request;

    public DownloadVideoTask(AmazonS3 client, String filename, DownloadVideoTaskListener listener) {
        this.client = client;
        this.filename = filename;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            // retrieves URL from S3
            request = new GeneratePresignedUrlRequest("guitar.tutor.data",
                    filename);
            URL url = client.generatePresignedUrl(request);

            return url.toString();
        }
        catch (AmazonClientException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String url) {
        if (url != null) {
            listener.onVideoDownloadSuccess(url);
        }
        else {
            listener.onVideoDownloadFailed();
        }
    }

}
