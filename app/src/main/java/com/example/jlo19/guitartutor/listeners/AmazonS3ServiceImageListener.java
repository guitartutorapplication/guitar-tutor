package com.example.jlo19.guitartutor.listeners;

import android.graphics.Bitmap;

/**
 * Listener for Amazon S3 Service (for images)
 */
public interface AmazonS3ServiceImageListener {
    void onImageDownloadFailed();
    void onImageDownloadSuccess(Bitmap bitmap);
}
