package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ValidationResult;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.views.EditAccountView;
import com.example.jlo19.guitartutor.views.IView;

import javax.inject.Inject;

/**
 * Presenter that provides the EditAccountActivity with ability to edit details on DB
 */
public class EditAccountPresenter implements IEditAccountPresenter {

    private EditAccountView view;
    private IEditAccountModel model;
    private SharedPreferences sharedPreferences;

    @Inject
    public void setModel(IEditAccountModel model) {
        this.model = model;
        this.model.setPresenter(this);
        this.model.setSharedPreferences(sharedPreferences);
    }

    @Override
    public void setView(IView view) {
        this.view = (EditAccountView) view;
        this.view.hideAccountButton();

        App.getComponent().inject(this);
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void viewOnSave(String name, String email, String confirmEmail, String password, String confirmPassword) {
        view.showProgressBar();
        model.save(name, email, confirmEmail, password, confirmPassword);
    }

    @Override
    public void modelOnSaveSuccess() {
        view.hideProgressBar();
        view.startAccountActivity();
    }

    @Override
    public void modelOnSaveError() {
        view.hideProgressBar();
        view.showSaveError();
    }

    @Override
    public void modelOnValidationFailed(ValidationResult result) {
        view.hideProgressBar();

        switch (result) {
            case FIELD_EMPTY:
                view.showFieldEmptyError();
                break;
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
        }
    }
}
