package com.example.jlo19.guitartutor.views;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.models.Chord;

import java.net.URL;

/**
 * View interface for ChordsActivity
 */
public interface ChordView {
    void showProgressBar();
    Chord getChord();
    Context getContext();
    void showError();
    void hideProgressBar();
    void setImage(Bitmap bitmap);
    void playVideo(URL url);
}
