package com.example.jlo19.guitartutor.models.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;

import java.util.ArrayList;

/**
 * Interface for PractiseSetupModel
 */
public interface IPractiseSetupModel {
    void setPresenter(IPractiseSetupPresenter presenter);
    void getChords();
    void chordsSelected(ArrayList<String> selectedChords, int chordChangeIndex, int beatSpeedIndex);
    void startBeatPreview(int beatSpeedIndex);
    void stopBeatPreview();
    void setSharedPreferences(SharedPreferences sharedPreferences);
}
