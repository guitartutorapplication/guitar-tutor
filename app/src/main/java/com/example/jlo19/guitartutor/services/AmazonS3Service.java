package com.example.jlo19.guitartutor.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.listeners.DownloadImageTaskListener;
import com.example.jlo19.guitartutor.tasks.DownloadImageTask;

/**
 * Manages calls to Amazon S3 storage
 */
public class AmazonS3Service implements DownloadImageTaskListener{

    private AmazonS3Client client;
    private AmazonS3ServiceListener listener;

    public AmazonS3Service(Context context, AmazonS3ServiceListener listener) {
        this.listener = listener;

        // setting up S3 client
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                context.getResources().getString(R.string.identity_pool_id),
                Regions.EU_WEST_1
        );

        client = new AmazonS3Client(credentialsProvider);
        client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.EU_WEST_1));
    }

    public void getImage(String filename) {
        new DownloadImageTask(client, filename, this).execute();
    }

    @Override
    public void onDownloadFailed() {
        // if task fails to retrieve image, report failure
        listener.onDownloadFailed();
    }

    @Override
    public void onDownloadSuccess(Bitmap bitmap) {
        // if task successfully retrieves image, send back image
        listener.onDownloadSuccess(bitmap);
    }
}
