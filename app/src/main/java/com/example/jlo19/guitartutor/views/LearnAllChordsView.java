package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

/**
 * View interface for LearnAllChordsActivity
 */
public interface LearnAllChordsView extends IProgressBarView {
    void showError();
    void startLearnChordActivity(Chord selectedChord, boolean learntChord);
    void addChordButton(int buttonId);
    void setChordButtonText(int buttonId, String text);
    void enableChordButton(int buttonId, boolean isEnabled);
    void setChordButtonBackground(int buttonId, String doneIdentifier, String levelNumberIdentifier);
    void setChordButtons();
    void finishActivity();
}
