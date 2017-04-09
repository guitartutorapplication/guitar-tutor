package com.example.jlo19.guitartutor.tasks;

import android.os.AsyncTask;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.jlo19.guitartutor.listeners.DownloadVideoTaskListener;

import java.net.URL;
import java.util.Date;

/**
 * Asynchronous task to retrieve video from Amazon S3
 */

public class DownloadVideoTask extends AsyncTask<Void, Void, String> {

    private final DownloadVideoTaskListener listener;
    private final String filename;
    private final AmazonS3 client;
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
            // setting the expiring time for pre-signed url
            Date expiration = new Date();
            long milliSeconds = expiration.getTime();
            // add one hour
            milliSeconds += 1000 * 60 * 60;
            expiration.setTime(milliSeconds);
            request.setExpiration(expiration);

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
