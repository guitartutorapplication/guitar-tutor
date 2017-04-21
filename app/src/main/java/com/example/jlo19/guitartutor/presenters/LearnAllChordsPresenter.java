package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
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
    private ILearnAllChordsModel model;

    @Override
    public void setView(IView view) {
        this.view = (LearnAllChordsView) view;
        this.view.showProgressBar();

        App.getComponent().inject(this);
    }

    @Inject
    void setModel(ILearnAllChordsModel model) {
        this.model = model;
        model.setPresenter(this);
        model.setSharedPreferences(sharedPreferences);
        model.getChordsAndDetails();
    }

    @Override
    public void modelOnChordsAndDetailsRetrieved() {
        view.hideProgressBar();

        List<Chord> allChords = model.getAllChords();
        int userLevel = model.getUserLevel();
        List<Integer> userChords = model.getUserChordIds();

        for (int i = 0; i < allChords.size(); i++) {
            view.addChordButton(i);
            view.setChordButtonText(i, allChords.get(i).toString());
            // button is only enabled if chord's level is than or equal to user's level
            view.enableChordButton(i, allChords.get(i).getLevelRequired() <= userLevel);
            // setting background based on level of chord/whether the user has learnt the chord
            boolean userHasLearntChord = userChords.contains(allChords.get(i).getId());

            String doneIdentifier = "";
            String levelNumberIdentifier;
            if (userHasLearntChord) {
                doneIdentifier = "done_";
            }

            if (allChords.get(i).getLevelRequired() == 1) {
                levelNumberIdentifier = "one";
            }
            else if (allChords.get(i).getLevelRequired() == 2) {
                levelNumberIdentifier = "two";
            }
            else if (allChords.get(i).getLevelRequired() == 3) {
                levelNumberIdentifier = "three";
            }
            else if (allChords.get(i).getLevelRequired() == 4) {
                levelNumberIdentifier = "four";
            }
            else if (allChords.get(i).getLevelRequired() == 5) {
                levelNumberIdentifier = "five";
            }
            else {
                levelNumberIdentifier = "six";
            }

            view.setChordButtonBackground(i, doneIdentifier, levelNumberIdentifier);
        }

        view.setChordButtons();
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

    @Override
    public void viewOnChordRequested(int chordPos) {
        Chord chord = model.getAllChords().get(chordPos);
        view.startLearnChordActivity(chord, model.getUserChordIds().contains(chord.getId()));
    }

    @Override
    public void viewOnConfirmError() {
        view.finishActivity();
    }
}
