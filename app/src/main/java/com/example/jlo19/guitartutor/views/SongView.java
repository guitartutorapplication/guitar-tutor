package com.example.jlo19.guitartutor.views;

import android.content.Context;

/**
 * Interface for SongActivity
 */
public interface SongView extends ProgressBarView {
    String getAudioFilename();
    void playAudio(String url);
    void showError();
    Context getContext();
    void setStopButtonVisibility(int visibility);
    void setPlayButtonVisibility(int visibility);
    void stopAudio();
}
