package com.example.jlo19.guitartutor.views;

/**
 * View interface for SongActivity
 */
public interface SongView extends ProgressBarView {
    String getAudioFilename();
    void playAudio(String url);
    void showError();
    void setStopButtonVisibility(int visibility);
    void setPlayButtonVisibility(int visibility);
    void stopAudio();
}
