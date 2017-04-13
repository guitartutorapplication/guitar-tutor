package com.example.jlo19.guitartutor.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceImageListener;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceUrlListener;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.tasks.DownloadImageTask;
import com.example.jlo19.guitartutor.tasks.DownloadUrlTask;

/**
 * Manages calls to Amazon S3 storage
 */
public class AmazonS3Service implements IAmazonS3Service {

    AmazonS3Client client;
    private AmazonS3ServiceImageListener imageListener;
    CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonS3ServiceUrlListener urlListener;

    public void getImage(String filename) {
        getDownloadImageTask(filename).execute();
    }

    public void getUrl(String filename) {
        getDownloadUrlTask(filename).execute();
    }

    @Override
    public void onImageDownloadFailed() {
        // if task fails to retrieve image, report failure
        imageListener.onImageDownloadFailed();
    }

    @Override
    public void onUrlDownloadSuccess(String url) {
        // if task successfully retrieves URL, send back URL
        urlListener.onUrlDownloadSuccess(url);
    }

    @Override
    public void onUrlDownloadFailed() {
        // if task fails to retrieve image, report failure
        urlListener.onUrlDownloadFailed();
    }

    @Override
    public void onImageDownloadSuccess(Bitmap bitmap) {
        // if task successfully retrieves image, send back image
        imageListener.onImageDownloadSuccess(bitmap);
    }

    public void setImageListener(AmazonS3ServiceImageListener listener) {
        this.imageListener = listener;
    }

    @Override
    public void setUrlListener(AmazonS3ServiceUrlListener listener) {
        this.urlListener = listener;
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

    DownloadUrlTask getDownloadUrlTask(String filename) {
        return new DownloadUrlTask(client, filename, this);
    }
}
