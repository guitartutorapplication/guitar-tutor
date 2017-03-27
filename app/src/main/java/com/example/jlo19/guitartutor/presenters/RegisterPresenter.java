package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.RegisterView;

import javax.inject.Inject;

/**
 * Presenter which provides RegisterActivity with the ability to add an account to DB
 */
public class RegisterPresenter implements IRegisterPresenter {

    private RegisterView view;
    private IRegisterModel model;

    public RegisterPresenter() {
        App.getComponent().inject(this);
    }

    @Inject
    public void setModel(IRegisterModel model) {
        this.model = model;
        model.setPresenter(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (RegisterView) view;
    }

    @Override
    public void viewOnRegister(String name, String email, String confirmEmail, String password,
                               String confirmPassword) {
        view.showProgressBar();
        model.register(name, email, confirmEmail, password, confirmPassword);
    }

    @Override
    public void modelOnFieldEmpty() {
        view.hideProgressBar();
        view.showFieldEmptyError();
    }

    @Override
    public void modelOnEmailMismatch() {
        view.hideProgressBar();
        view.showEmailMismatchError();
    }

    @Override
    public void modelOnPasswordMismatch() {
        view.hideProgressBar();
        view.showPasswordMismatchError();
    }

    @Override
    public void modelOnInvalidEmail() {
        view.hideProgressBar();
        view.showInvalidEmailError();
    }

    @Override
    public void modelOnPasswordTooShort() {
        view.hideProgressBar();
        view.showPasswordTooShortError();
    }

    @Override
    public void modelOnPasswordNoUpperCaseLetter() {
        view.hideProgressBar();
        view.showPasswordNoUpperCaseLetterError();
    }

    @Override
    public void modelOnPasswordNoLowerCaseLetter() {
        view.hideProgressBar();
        view.showPasswordNoLowerCaseLetterError();
    }

    @Override
    public void modelOnPasswordNoNumber() {
        view.hideProgressBar();
        view.showPasswordNoNumberError();
    }

    @Override
    public void modelOnRegisterError() {
        view.hideProgressBar();
        view.showRegisterError();
    }

    @Override
    public void modelOnAlreadyRegistered() {
        view.hideProgressBar();
        view.showAlreadyRegisteredError();
    }

    @Override
    public void modelOnRegisterSuccess() {
        view.hideProgressBar();
        view.startHomeActivity();
    }
}
