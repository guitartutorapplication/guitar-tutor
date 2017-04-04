package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LearnAllChordsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter which provides the activities with all chords from the database API
 */
public class LearnAllChordsPresenter implements ILearnAllChordsPresenter {

    private LearnAllChordsView view;
    private SharedPreferences sharedPreferences;

    @Override
    public void setView(IView view) {
        this.view = (LearnAllChordsView) view;
        this.view.showProgressBar();

        App.getComponent().inject(this);
    }

    @Inject
    void setModel(ILearnAllChordsModel model) {
        model.setPresenter(this);
        model.setSharedPreferences(sharedPreferences);
        model.getChords();
    }

    @Override
    public void modelOnChordsRetrieved(List<Chord> chords, List<Integer> userChords) {
        view.hideProgressBar();
        view.setChords(chords, userChords);
    }

    @Override
    public void modelOnError() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
