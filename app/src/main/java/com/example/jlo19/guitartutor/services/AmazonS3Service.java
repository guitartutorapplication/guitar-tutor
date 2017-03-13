package com.example.jlo19.guitartutor.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.tasks.DownloadImageTask;
import com.example.jlo19.guitartutor.tasks.DownloadVideoTask;

/**
 * Manages calls to Amazon S3 storage
 */
public class AmazonS3Service implements IAmazonS3Service {

    AmazonS3Client client;
    private AmazonS3ServiceListener listener;
    CognitoCachingCredentialsProvider credentialsProvider;

    public void getImage(String filename) {
        getDownloadImageTask(filename).execute();
    }

    public void getVideo(String filename) {
        getDownloadVideoTask(filename).execute();
    }

    @Override
    public void onImageDownloadFailed() {
        // if task fails to retrieve image, report failure
        listener.onImageDownloadFailed();
    }

    @Override
    public void onVideoDownloadSuccess(String url) {
        // if task successfully retrieves URL, send back URL
        listener.onVideoDownloadSuccess(url);
    }

    @Override
    public void onVideoDownloadFailed() {
        // if task fails to retrieve image, report failure
        listener.onVideoDownloadFailed();
    }

    @Override
    public void onImageDownloadSuccess(Bitmap bitmap) {
        // if task successfully retrieves image, send back image
        listener.onImageDownloadSuccess(bitmap);
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
    }

    DownloadImageTask getDownloadImageTask(String filename) {
        return new DownloadImageTask(client, filename, this);
    }

    DownloadVideoTask getDownloadVideoTask(String filename) {
        return  new DownloadVideoTask(client, filename, this);
    }
}
