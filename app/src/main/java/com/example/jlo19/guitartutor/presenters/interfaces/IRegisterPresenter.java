package com.example.jlo19.guitartutor.presenters.interfaces;

/**
 * Interface for RegisterPresenter
 */
public interface IRegisterPresenter extends IPresenter {
    void viewOnRegister(String name, String email, String confirmEmail, String password, String confirmPassword);
    void modelOnFieldEmpty();
    void modelOnEmailMismatch();
    void modelOnPasswordMismatch();
    void modelOnInvalidEmail();
    void modelOnPasswordTooShort();
    void modelOnPasswordNoUpperCaseLetter();
    void modelOnPasswordNoLowerCaseLetter();
    void modelOnPasswordNoNumber();
    void modelOnRegisterError();
    void modelOnAlreadyRegistered();
    void modelOnRegisterSuccess();
}
