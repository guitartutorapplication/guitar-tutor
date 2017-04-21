package com.example.jlo19.guitartutor.views;

/**
 * Represents common view behaviour for data validation
 */
interface IDataValidationView extends IProgressBarView{
    void showFieldEmptyNameError();
    void showEmailMismatchError();
    void showPasswordMismatchError();
    void showInvalidEmailError();
    void showPasswordTooShortError();
    void showPasswordNoUpperCaseLetterError();
    void showPasswordNoLowerCaseLetterError();
    void showPasswordNoNumberError();
    void showAlreadyRegisteredError();
    void showFieldEmptyEmailError();
    void showFieldEmptyConfirmEmailError();
    void showFieldEmptyPasswordError();
    void showFieldEmptyConfirmPasswordError();
    void resetValidationErrors();
}
