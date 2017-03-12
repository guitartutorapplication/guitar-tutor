package com.example.jlo19.guitartutor.views;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.models.retrofit.Chord;

/**
 * View interface for ChordsActivity
 */
public interface LearnChordView extends IView{
    void showProgressBar();
    Chord getChord();
    Context getContext();
    void showError();
    void hideProgressBar();
    void setImage(Bitmap bitmap);
    void playVideo(String url);
}
