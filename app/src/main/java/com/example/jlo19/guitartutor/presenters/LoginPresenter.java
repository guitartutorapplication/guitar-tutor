package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.LoginView;

import javax.inject.Inject;

/**
 * Presenter which provides LoginActivity with the ability to verify login credentials with DB
 */
public class LoginPresenter implements ILoginPresenter {

    private LoginView view;
    private ILoginModel model;

    public LoginPresenter() {
        App.getComponent().inject(this);
    }

    @Inject
    public void setModel(ILoginModel model) {
        this.model = model;
        model.setPresenter(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (LoginView) view;
    }

    @Override
    public void viewOnRegister() {
        view.startRegisterActivity();
    }

    @Override
    public void viewOnLogin(String email, String password) {
        view.showProgressBar();
        model.login(email, password);
    }

    @Override
    public void modelOnFieldEmpty() {
        view.hideProgressBar();
        view.showFieldEmptyError();
    }

    @Override
    public void modelOnLoginSuccess() {
        view.hideProgressBar();
        view.startHomeActivity();
    }

    @Override
    public void modelOnIncorrectCredentials() {
        view.hideProgressBar();
        view.showIncorrectCredentialsError();
    }

    @Override
    public void modelOnLoginError() {
        view.hideProgressBar();
        view.showLoginError();
    }
}
