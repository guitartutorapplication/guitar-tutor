package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.views.AccountActivityView;
import com.example.jlo19.guitartutor.views.IView;

import java.util.List;

/**
 * Presenter that provides the AccountActivityActivity with account details from DB
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

        getUserChordsInteractor.getUserChords(loggedInUser.getApiKey(), loggedInUser.getUserId());
    }

    @Override
    public void onError() {
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
