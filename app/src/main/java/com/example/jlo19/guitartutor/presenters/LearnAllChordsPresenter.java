package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnViewAllChordsPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LearnAllChordsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter which provides the activities with all chords from the database API
 */
public class LearnAllChordsPresenter implements ILearnViewAllChordsPresenter {

    private LearnAllChordsView view;
    private ILearnViewAllChordsModel model;

    public LearnAllChordsPresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (LearnAllChordsView) view;
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
