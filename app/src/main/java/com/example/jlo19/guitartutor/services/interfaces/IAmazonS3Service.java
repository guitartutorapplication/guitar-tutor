package com.example.jlo19.guitartutor.services.interfaces;

import android.content.Context;

import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.listeners.DownloadImageTaskListener;
import com.example.jlo19.guitartutor.listeners.DownloadVideoTaskListener;

/**
 * Interface for AmazonS3Service
 */
public interface IAmazonS3Service extends DownloadImageTaskListener, DownloadVideoTaskListener {
    void setListener(AmazonS3ServiceListener listener);
    void setClient(Context context);
    void getImage(String filename);
    void getVideo(String filename);
}
