package com.example.jlo19.guitartutor.listeners;

/**
 * Listener for DownloadVideoTask
 */

public interface DownloadVideoTaskListener {
    void onVideoDownloadSuccess(String url);
    void onVideoDownloadFailed();
}
