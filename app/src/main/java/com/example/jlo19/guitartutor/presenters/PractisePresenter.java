package com.example.jlo19.guitartutor.presenters;

import android.view.View;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.PractiseView;

import javax.inject.Inject;

/**
 * Presenter which provides the PractiseActivity with timer capabilities
 */
public class PractisePresenter implements IPractisePresenter {

    private PractiseView view;
    private IPractiseModel model;

    @Override
    public void setView(IView view) {
        this.view = (PractiseView) view;
        this.view.setToolbarTitleText();
        this.view.setChordText(this.view.getSelectedChords().get(0));
        this.view.loadSound();

        App.getComponent().inject(this);
    }

    @Inject
    void setModel(IPractiseModel model) {
        this.model = model;
        model.setPresenter(this);
        model.setSelectedChords(view.getSelectedChords());
        model.setChordChange(view.getChordChange());
        model.createTimer();
    }

    @Override
    public void modelOnNewChord(String chord) {
        view.setChordText(chord);
    }

    @Override
    public void viewOnStartPractising() {
        model.start();
        view.setStopButtonVisibility(View.VISIBLE);
        view.setStartButtonVisibility(View.INVISIBLE);
    }

    @Override
    public void viewOnStopPractising() {
        model.stop();
        view.setChordText(view.getSelectedChords().get(0));
        view.setStopButtonVisibility(View.INVISIBLE);
        view.setStartButtonVisibility(View.VISIBLE);
    }

    @Override
    public void modelOnNewSecond() {
        view.playSound();
    }

    @Override
    public void modelOnError() {
        view.showError();
        view.startPractiseSetupActivity();
    }
}
