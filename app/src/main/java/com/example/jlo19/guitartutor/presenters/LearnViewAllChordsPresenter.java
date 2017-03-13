package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnViewAllChordsPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LearnViewAllChordsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter which provides the activities with all chords from the database API
 */
public class LearnViewAllChordsPresenter implements ILearnViewAllChordsPresenter {

    private LearnViewAllChordsView view;
    private ILearnViewAllChordsModel model;

    public LearnViewAllChordsPresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (LearnViewAllChordsView) view;
        this.view.setToolbarTitleText();
        this.view.showProgressBar();

        model.getChords();
    }

    @Inject
    void setModel(ILearnViewAllChordsModel model) {
        this.model = model;
        model.setPresenter(this);
    }

    @Override
    public void modelOnChordsRetrieved(List<Chord> chords) {
        view.hideProgressBar();
        view.setChords(chords);
    }

    @Override
    public void modelOnError() {
        view.hideProgressBar();
        view.showError();
    }
}
