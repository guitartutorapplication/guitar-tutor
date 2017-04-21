package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.views.IView;
import com.example.jlo19.guitartutor.views.RegisterView;

import java.util.List;

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
        view.resetValidationErrors();
        model.register(name, email, confirmEmail, password, confirmPassword);
    }

    @Override
    public void modelOnRegisterError() {
        view.hideProgressBar();
        view.showRegisterError();
    }

    @Override
    public void modelOnRegisterSuccess() {
        view.hideProgressBar();
        view.finishRegister();
    }

    @Override
    public void modelOnValidationFailed(List<ValidationError> errors) {
        view.hideProgressBar();

        for (ValidationError error : errors) {
            switch (error) {
                case FIELD_EMPTY_NAME:
                    view.showFieldEmptyNameError();
                    break;
                case FIELD_EMPTY_EMAIL:
                    view.showFieldEmptyEmailError();
                    break;
                case FIELD_EMPTY_CONFIRM_EMAIL:
                    view.showFieldEmptyConfirmEmailError();
                    break;
                case FIELD_EMPTY_PASSWORD:
                    view.showFieldEmptyPasswordError();
                    break;
                case FIELD_EMPTY_CONFIRM_PASSWORD:
                    view.showFieldEmptyConfirmPasswordError();
                case EMAIL_MISMATCH:
                    view.showEmailMismatchError();
                    break;
                case PASSWORD_MISMATCH:
                    view.showPasswordMismatchError();
                    break;
                case INVALID_EMAIL:
                    view.showInvalidEmailError();
                    break;
                case PASSWORD_TOO_SHORT:
                    view.showPasswordTooShortError();
                    break;
                case PASSWORD_NO_UPPER:
                    view.showPasswordNoUpperCaseLetterError();
                    break;
                case PASSWORD_NO_LOWER:
                    view.showPasswordNoLowerCaseLetterError();
                    break;
                case PASSWORD_NO_NUMBER:
                    view.showPasswordNoNumberError();
                    break;
                case EMAIL_ALREADY_REGISTERED:
                    view.showAlreadyRegisteredError();
                    break;
            }
        }
    }
}
