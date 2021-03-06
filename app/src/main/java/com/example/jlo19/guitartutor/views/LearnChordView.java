package com.example.jlo19.guitartutor.views;

import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.models.Chord;

/**
 * View interface for ChordsActivity
 */
public interface LearnChordView extends ProgressBarView {
    Chord getChord();
    void showImageLoadError();
    void setImage(Bitmap bitmap);
    void playVideo(String url);
    void showVideoLoadError();
    boolean getLearntChord();
    void enableLearntButton(boolean isEnabled);
    void showLearntConfirmDialog();
    void startLearnAllChordsActivity();
    void showAddLearntChordError();
    void showAddLearntChordSuccess();
    void showAddLearntChordSuccess(int level, int achievements);
    void showAddLearntChordSuccess(int achievements);
    void finishActivity();
    void startDiagramHelpActivity();
}
