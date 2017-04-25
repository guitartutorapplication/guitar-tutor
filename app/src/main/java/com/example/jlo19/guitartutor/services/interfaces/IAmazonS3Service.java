package com.example.jlo19.guitartutor.services.interfaces;

import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceImageListener;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceUrlListener;
import com.example.jlo19.guitartutor.listeners.DownloadImageTaskListener;
import com.example.jlo19.guitartutor.listeners.DownloadVideoTaskListener;

/**
 * Interface for AmazonS3Service
 */
public interface IAmazonS3Service extends DownloadImageTaskListener, DownloadVideoTaskListener {
    void setImageListener(AmazonS3ServiceImageListener listener);
    void setUrlListener(AmazonS3ServiceUrlListener listener);
    void getImage(String filename);
    void getUrl(String filename);
}
