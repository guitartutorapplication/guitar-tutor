package com.example.jlo19.guitartutor.listeners;

import android.graphics.Bitmap;

/**
 * Listener for AmazonS3Service
 */
public interface AmazonS3ServiceListener {
    void onImageDownloadFailed();
    void onImageDownloadSuccess(Bitmap bitmap);
    void onVideoDownloadSuccess(String url);
    void onVideoDownloadFailed();
}
