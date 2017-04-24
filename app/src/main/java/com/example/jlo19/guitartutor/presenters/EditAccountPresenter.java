package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.validation.DataValidator;
import com.example.jlo19.guitartutor.views.EditAccountView;
import com.example.jlo19.guitartutor.views.IView;

import java.util.List;

/**
 * Presenter that provides the EditAccountActivity with ability to edit details on DB
 */
public class EditAccountPresenter implements IEditAccountPresenter {

    private final LoggedInUser loggedInUser;
    private EditAccountView view;
    private final IEditAccountDetailsInteractor editAccountDetailsInteractor;

    public EditAccountPresenter(IEditAccountDetailsInteractor editAccountDetailsInteractor,
                                LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.editAccountDetailsInteractor = editAccountDetailsInteractor;
        this.editAccountDetailsInteractor.setListener(this);
    }

    @Override
    public void setView(IView view) {
        this.view = (EditAccountView) view;
        this.view.hideAccountButton();
    }

    @Override
    public void viewOnSave(String name, String email, String confirmEmail, String password, String confirmPassword) {
        view.showProgressBar();
        view.resetValidationErrors();

        List<ValidationError> errors = DataValidator.validate(name, email, confirmEmail, password,
                confirmPassword);
        if (errors.isEmpty()) {
            editAccountDetailsInteractor.save(loggedInUser.getApiKey(), loggedInUser.getUserId(),
                    name, email, password);
        }
        else {
            onValidationFailed(errors);
        }
    }

    @Override
    public void onSaveSuccess() {
        view.hideProgressBar();
        view.startAccountActivity();
    }

    @Override
    public void onSaveError() {
        view.hideProgressBar();
        view.showSaveError();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
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
