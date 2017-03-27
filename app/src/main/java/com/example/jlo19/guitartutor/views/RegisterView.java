package com.example.jlo19.guitartutor.views;

/**
 * View interface for RegisterActivity
 */

public interface RegisterView extends IView {
    void showFieldEmptyError();
    void showEmailMismatchError();
    void showPasswordMismatchError();
    void showInvalidEmailError();
    void showPasswordTooShortError();
    void showPasswordNoUpperCaseLetterError();
    void showPasswordNoLowerCaseLetterError();
    void showPasswordNoNumberError();
    void showRegisterError();
    void showAlreadyRegisteredError();
    void startLoginActivity();
    void showProgressBar();
    void hideProgressBar();
}
