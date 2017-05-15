package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.interactors.interfaces.ILoginInteractor;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LoginView;

/**
 * Presenter that provides LoginActivity with DB API interaction
 */
public class LoginPresenter implements ILoginPresenter {

    private final LoggedInUser loggedInUser;
    private LoginView view;
    private final ILoginInteractor loginInteractor;

    public LoginPresenter(ILoginInteractor loginInteractor, LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.loginInteractor = loginInteractor;
        this.loginInteractor.setListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (LoginView) view;

        if (loggedInUser.isLoggedIn()) {
            // if user is already logged in, redirect to home activity
            this.view.startHomeActivity();
        }
    }

    @Override
    public void viewOnRegister() {
        view.startRegisterActivity();
    }

    @Override
    public void viewOnLogin(String email, String password) {
        view.resetFieldEmptyErrors();

        if (!email.isEmpty() && !password.isEmpty()) {
            // if email and password is present, log in to account on DB
            view.showProgressBar();
            loginInteractor.login(email, password);
        }
        else {
            // if not valid show error
            if (email.isEmpty()) {
                view.showFieldEmailEmptyError();
            }
            if (password.isEmpty()) {
                view.showFieldPasswordEmptyError();
            }
        }
    }

    @Override
    public void onLoginSuccess(User user) {
        // user becomes the logged in user and then is redirected to home
        loggedInUser.login(user.getId(), user.getApiKey());
        view.hideProgressBar();
        view.startHomeActivity();
    }

    @Override
    public void onLoginError() {
        view.hideProgressBar();
        view.showLoginError();
    }
}
