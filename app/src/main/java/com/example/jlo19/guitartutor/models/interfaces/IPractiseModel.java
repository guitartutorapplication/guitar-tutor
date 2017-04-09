package com.example.jlo19.guitartutor.models.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;

import java.util.List;

/**
 * Interface for PractiseModel
 */
public interface IPractiseModel {
    void setSelectedChords(List<Chord> selectedChords);
    void createPractiseTimer();
    void setPresenter(IPractisePresenter presenter);
    void startPractiseTimer();
    void stopTimer();
    void setChordChange(ChordChange chordChange);
    void setBeatSpeed(BeatSpeed beatSpeed);
    void startCountdown();
    void savePractiseSession();
    void setSharedPreferences(SharedPreferences sharedPreferences);
}
