package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.PractiseSetupView;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter which provides PractiseSetupActivity with chords from database API and timer capabilities
 */
public class PractiseSetupPresenter implements IPractiseSetupPresenter {

    private PractiseSetupView view;
    private IPractiseSetupModel model;
    private SharedPreferences sharedPreferences;

    @Override
    public void setView(IView view) {
        this.view = (PractiseSetupView) view;
        this.view.showProgressBar();
        this.view.loadSound();

        App.getComponent().inject(this);
    }

    @Inject
    void setModel(IPractiseSetupModel model) {
        this.model = model;
        model.setPresenter(this);
        model.setSharedPreferences(sharedPreferences);
        model.getChords();
    }

    @Override
    public void modelOnChordsRetrieved(List<Chord> chords) {
        view.hideProgressBar();
        view.setChords(chords);
    }

    @Override
    public void modelOnLoadChordsError() {
        view.hideProgressBar();
        view.showLoadChordsError();
    }

    @Override
    public void viewOnPractise(List<Chord> selectedChords, int chordChangeIndex, int beatSpeedIndex) {
        model.chordsSelected(selectedChords, chordChangeIndex, beatSpeedIndex);
    }

    @Override
    public void modelOnLessThanTwoChordsSelected() {
        view.showLessThanTwoChordsSelectedError();
    }

    @Override
    public void modelOnSameSelectedChord() {
        view.showSameSelectedChordError();
    }

    @Override
    public void modelOnCorrectSelectedChords(List<Chord> selectedChords, ChordChange chordChange, BeatSpeed beatSpeed) {
        view.startPractiseActivity(selectedChords, chordChange, beatSpeed);
    }

    @Override
    public void viewOnBeatPreview(int beatSpeedIndex) {
        view.enablePreviewButton(false);
        model.startBeatPreview(beatSpeedIndex);
    }

    @Override
    public void modelOnNewBeat() {
        view.playSound();
    }

    @Override
    public void modelOnPreviewBeatError() {
        view.showPreviewBeatError();
    }

    @Override
    public void modelOnBeatPreviewFinished() {
        view.enablePreviewButton(true);
    }

    @Override
    public void viewOnBeatSpeedChanged() {
        model.stopBeatPreview();
    }

    @Override
    public void viewOnDestroy() {
        model.stopBeatPreview();
    }

    @Override
    public void viewOnPause() {
        model.stopBeatPreview();
    }

    @Override
    public void viewOnStop() {
        model.stopBeatPreview();
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
