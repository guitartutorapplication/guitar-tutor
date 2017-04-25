package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceUrlListener;

/**
 * Interface for SongPresenter
 */
public interface ISongPresenter extends IPresenter, AmazonS3ServiceUrlListener {
    void viewOnPlay();
    void viewOnAudioLoadFailed();
    void viewOnAudioLoaded();
    void viewOnStop();
}
