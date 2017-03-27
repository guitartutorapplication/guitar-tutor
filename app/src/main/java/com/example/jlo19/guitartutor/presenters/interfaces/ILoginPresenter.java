package com.example.jlo19.guitartutor.presenters.interfaces;

/**
 * Interface for LoginPresenter
 */
public interface ILoginPresenter extends IPresenter {
    void viewOnRegister();
    void viewOnLogin(String email, String password);
    void modelOnFieldEmpty();
    void modelOnLoginSuccess();
    void modelOnIncorrectCredentials();
    void modelOnLoginError();
}
