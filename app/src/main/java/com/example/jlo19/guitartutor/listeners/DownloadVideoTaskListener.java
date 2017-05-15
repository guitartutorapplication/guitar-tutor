package com.example.jlo19.guitartutor.listeners;

/**
 * Listener for DownloadUrlTask
 */
public interface DownloadVideoTaskListener {
    void onUrlDownloadSuccess(String url);
    void onUrlDownloadFailed();
}
