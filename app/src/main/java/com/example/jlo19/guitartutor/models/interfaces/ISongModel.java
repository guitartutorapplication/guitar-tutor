package com.example.jlo19.guitartutor.models.interfaces;

import android.content.Context;

import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceUrlListener;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;

/**
 * Interface for SongModel
 */
public interface ISongModel extends AmazonS3ServiceUrlListener {
    void setPresenter(ISongPresenter presenter);
    void getAudio(String filename);
    void setContext(Context context);
}
