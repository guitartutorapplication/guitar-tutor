package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.PractiseSetupView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Presenter which provides PractiseSetupActivity with chords from database API
 */
public class PractiseSetupPresenter implements IPractiseSetupPresenter {

    private PractiseSetupView view;
    private IPractiseSetupModel model;

    public PractiseSetupPresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (PractiseSetupView) view;
        this.view.setToolbarTitleText();
        this.view.showProgressBar();
        this.view.loadSound();

        model.getChords();
    }

    @Inject
    void setModel(IPractiseSetupModel model) {
        this.model = model;
        model.setPresenter(this);
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
    public void viewOnPractise(ArrayList<String> selectedChords, int chordChangeIndex, int beatSpeedIndex) {
        model.stopBeatPreview();
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
    public void modelOnCorrectSelectedChords(ArrayList<String> selectedChords, ChordChange chordChange, BeatSpeed beatSpeed) {
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
}
