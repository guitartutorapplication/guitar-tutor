package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LearnAllChordsView;

import java.util.List;

/**
 * Presenter that provides LearnAllChordsActivity with DB API interaction
 */
public class LearnAllChordsPresenter implements ILearnAllChordsPresenter {

    private LearnAllChordsView view;
    private final IGetChordsInteractor getChordsInteractor;
    private final LoggedInUser loggedInUser;

    public LearnAllChordsPresenter(IGetChordsInteractor getChordsInteractor, LoggedInUser loggedInUser) {
        this.getChordsInteractor = getChordsInteractor;
        this.loggedInUser = loggedInUser;
        this.getChordsInteractor.setListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (LearnAllChordsView) view;
        this.view.showProgressBar();

        // gets chords (and other details) from DB
        this.getChordsInteractor.getChordsAndDetails(loggedInUser.getApiKey(), loggedInUser.getUserId());
    }

    @Override
    public void onChordsAndDetailsRetrieved(
            List<Chord> allChords, int userLevel, List<Integer> userChords) {
        view.hideProgressBar();

        for (int i = 0; i < allChords.size(); i++) {
            view.addChordButton(i);
            view.setChordButtonText(i, allChords.get(i).toString());
            // button is only enabled if chord's level is less than or equal to user's level
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
    public void onError() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void viewOnChordRequested(int chordPos) {
        // retrieve selected chord and start learn chord activity
        Chord chord = getChordsInteractor.getAllChords().get(chordPos);
        view.startLearnChordActivity(chord, getChordsInteractor.getUserChordIds().contains(chord.getId()));
    }

    @Override
    public void viewOnConfirmError() {
        view.finishActivity();
    }
}
