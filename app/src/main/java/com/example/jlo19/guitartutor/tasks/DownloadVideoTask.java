package com.example.jlo19.guitartutor.tasks;

import android.os.AsyncTask;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.jlo19.guitartutor.listeners.DownloadVideoTaskListener;

/**
 * Asynchronous task to retrieve video from Amazon S3
 */

public class DownloadVideoTask extends AsyncTask<Void, Void, String> {

    private DownloadVideoTaskListener listener;
    private String filename;
    private AmazonS3 client;
    GeneratePresignedUrlRequest request;

    public void setClient(AmazonS3 client) {
        this.client = client;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setListener(DownloadVideoTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        // retrieves URL from S3
        request = new GeneratePresignedUrlRequest("guitar.tutor.data",
                filename);

        return client.generatePresignedUrl(request).toString();
    }

    @Override
    protected void onPostExecute(String url) {
        listener.onDownloadSuccess(url);
    }

}
