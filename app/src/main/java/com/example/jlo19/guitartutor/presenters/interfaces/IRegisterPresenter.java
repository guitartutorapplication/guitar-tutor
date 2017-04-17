package com.example.jlo19.guitartutor.presenters.interfaces;

/**
 * Interface for RegisterPresenter
 */
public interface IRegisterPresenter extends IDataValidationPresenter {
    void viewOnRegister(String name, String email, String confirmEmail, String password, String confirmPassword);
    void modelOnRegisterError();
    void modelOnRegisterSuccess();
}
