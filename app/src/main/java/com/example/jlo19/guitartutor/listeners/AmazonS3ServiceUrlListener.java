package com.example.jlo19.guitartutor.listeners;

/**
 * Listener for Amazon S3 Service (for URLs)
 */
public interface AmazonS3ServiceUrlListener {
    void onUrlDownloadSuccess(String url);
    void onUrlDownloadFailed();
}
