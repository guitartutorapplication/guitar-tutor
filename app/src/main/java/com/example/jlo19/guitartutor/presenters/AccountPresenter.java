package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.IAccountModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.views.AccountView;
import com.example.jlo19.guitartutor.views.IView;

import javax.inject.Inject;

/**
 * Presenter that provides the AccountActivity with account details from DB
 */
public class AccountPresenter implements IAccountPresenter {

    private SharedPreferences sharedPreferences;
    private AccountView view;
    private IAccountModel model;

    @Inject
    public void setModel(IAccountModel model) {
        this.model = model;
        this.model.setPresenter(this);
        this.model.setSharedPreferences(sharedPreferences);
        this.model.getAccountDetails();
    }

    @Override
    public void setView(IView view) {
        this.view = (AccountView) view;
        this.view.hideAccountButton();
        this.view.showProgressBar();

        App.getComponent().inject(this);
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void modelOnAccountDetailsRetrieved(User user) {
        view.hideProgressBar();
        view.setAccountDetails(user);
    }

    @Override
    public void modelOnError() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void viewOnLogout() {
        model.logout();
        view.startLoginActivity();
    }

    @Override
    public void viewOnEditAccount() {
        view.startEditAccountActivity();
    }

    @Override
    public void viewOnAccountActivityRequested() {
        view.startAccountActivityActivity();
    }
}
