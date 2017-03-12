package com.example.jlo19.guitartutor.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.tasks.DownloadImageTask;
import com.example.jlo19.guitartutor.tasks.DownloadVideoTask;

import javax.inject.Inject;

/**
 * Manages calls to Amazon S3 storage
 */
public class AmazonS3Service implements IAmazonS3Service {

    AmazonS3Client client;
    private AmazonS3ServiceListener listener;
    CognitoCachingCredentialsProvider credentialsProvider;
    private DownloadImageTask downloadImageTask;
    private DownloadVideoTask downloadVideoTask;

    public AmazonS3Service() {
        App.getComponent().inject(this);
    }

    @Inject
    void setDownloadImageTask(DownloadImageTask task) {
        this.downloadImageTask = task;
        downloadImageTask.setListener(this);
    }

    @Inject
    void setDownloadVideoTask(DownloadVideoTask task) {
        this.downloadVideoTask = task;
        downloadVideoTask.setListener(this);
    }

    public void getImage(String filename) {
        downloadImageTask.setFilename(filename);
        downloadImageTask.execute();
    }

    public void getVideo(String filename) {
        downloadVideoTask.setFilename(filename);
        downloadVideoTask.execute();
    }

    @Override
    public void onDownloadFailed() {
        // if task fails to retrieve image, report failure
        listener.onDownloadFailed();
    }

    @Override
    public void onDownloadSuccess(String url) {
        // if task successfully retrieves URL, send back URL
        listener.onDownloadSuccess(url);
    }

    @Override
    public void onDownloadSuccess(Bitmap bitmap) {
        // if task successfully retrieves image, send back image
        listener.onDownloadSuccess(bitmap);
    }

    public void setListener(AmazonS3ServiceListener listener) {
        this.listener = listener;
    }

    public void setClient(Context context) {
        // setting up S3 client
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                context.getResources().getString(R.string.identity_pool_id),
                Regions.EU_WEST_1
        );
        client = new AmazonS3Client(credentialsProvider);
        client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.EU_WEST_1));
        downloadImageTask.setClient(client);
        downloadVideoTask.setClient(client);
    }
}
