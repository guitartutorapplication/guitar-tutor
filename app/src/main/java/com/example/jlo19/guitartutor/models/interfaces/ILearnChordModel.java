package com.example.jlo19.guitartutor.models.interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceListener;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;

/**
 * Interface for LearnChordModel
 */
public interface ILearnChordModel extends AmazonS3ServiceListener {
    void setContext(Context context);
    void getImage(String filename);
    void setPresenter(ILearnChordPresenter presenter);
    void getVideo(String filename);
    void addLearntChord(int chordId);
    void setSharedPreferences(SharedPreferences sharedPreferences);
}
