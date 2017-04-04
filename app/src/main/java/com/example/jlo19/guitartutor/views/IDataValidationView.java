package com.example.jlo19.guitartutor.views;

/**
 * Represents common view behaviour for data validation
 */
interface IDataValidationView extends IProgressBarView{
    void showFieldEmptyError();
    void showEmailMismatchError();
    void showPasswordMismatchError();
    void showInvalidEmailError();
    void showPasswordTooShortError();
    void showPasswordNoUpperCaseLetterError();
    void showPasswordNoLowerCaseLetterError();
    void showPasswordNoNumberError();
}
