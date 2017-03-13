package com.example.jlo19.guitartutor.listeners;

import android.graphics.Bitmap;

/**
 * Listener for DownloadImageTask
 */

public interface DownloadImageTaskListener {
    void onImageDownloadFailed();
    void onImageDownloadSuccess(Bitmap bitmap);
}
