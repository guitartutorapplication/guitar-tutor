package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.views.AccountActivityView;
import com.example.jlo19.guitartutor.views.IView;

import java.util.List;

/**
 * Presenter that provides AccountActivityActivity with DB API interaction
 */
public class AccountActivityPresenter implements IAccountActivityPresenter {

    private final LoggedInUser loggedInUser;
    private final IGetUserChordsInteractor getUserChordsInteractor;
    private AccountActivityView view;

    public AccountActivityPresenter(IGetUserChordsInteractor getUserChordsInteractor, LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.getUserChordsInteractor = getUserChordsInteractor;
        this.getUserChordsInteractor.setListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (AccountActivityView) view;
        this.view.showProgressBar();

        // get user chords from DB (returns chords and num of times each is practised - activity)
        getUserChordsInteractor.getUserChords(loggedInUser.getApiKey(), loggedInUser.getUserId());
    }

    @Override
    public void onGetUserChordsError() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void onUserChordsRetrieved(List<Chord> chords) {
        view.hideProgressBar();
        view.setAccountActivity(chords);
    }

    @Override
    public void viewOnConfirmError() {
        view.finishActivity();
    }
}
