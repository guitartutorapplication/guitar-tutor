package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.views.AccountView;
import com.example.jlo19.guitartutor.views.IView;

/**
 * Presenter that provides AccountActivity with DB API interaction
 */
public class AccountPresenter implements IAccountPresenter {

    private AccountView view;
    private final IGetAccountDetailsInteractor getAccountDetailsInteractor;
    private final LoggedInUser loggedInUser;
    private User user;

    public AccountPresenter(IGetAccountDetailsInteractor getAccountDetailsInteractor, LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.getAccountDetailsInteractor = getAccountDetailsInteractor;
        this.getAccountDetailsInteractor.setListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (AccountView) view;
        this.view.hideAccountButton();
        this.view.showProgressBar();

        // get account details from DB
        getAccountDetailsInteractor.getAccountDetails(loggedInUser.getApiKey(),
                loggedInUser.getUserId());
    }

    @Override
    public void onAccountDetailsRetrieved(User user) {
        this.user = user;
        view.hideProgressBar();
        view.setAccountDetails(user.getName(), user.getEmail(), user.getLevel(), user.getAchievements());
    }

    @Override
    public void onGetAccountDetailsError() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void viewOnLogout() {
        loggedInUser.logout();
        view.startLoginActivity();
    }

    @Override
    public void viewOnEditAccount() {
        view.startEditAccountActivity(user);
    }

    @Override
    public void viewOnAccountActivityRequested() {
        view.startAccountActivityActivity();
    }

    @Override
    public void viewOnConfirmError() {
        view.finishActivity();
    }
}
