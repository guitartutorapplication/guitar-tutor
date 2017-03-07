package com.example.jlo19.guitartutor.listeners;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Listener for AmazonS3Service
 */
public interface AmazonS3ServiceListener {
    void onDownloadFailed();
    void onDownloadSuccess(Bitmap bitmap);
    void onDownloadSuccess(URL url);
}
