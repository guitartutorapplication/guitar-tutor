package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.ILoginInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LoginView;

/**
 * Presenter which provides LoginActivity with the ability to verify login credentials with DB
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
            ((LoginView) view).startHomeActivity();
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
            view.showProgressBar();
            loginInteractor.login(email, password);
        }
        else {
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
