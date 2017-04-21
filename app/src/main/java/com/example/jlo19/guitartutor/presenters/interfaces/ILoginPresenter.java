package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

/**
 * Interface for LoginPresenter
 */
public interface ILoginPresenter extends IPresenter {
    void viewOnRegister();
    void viewOnLogin(String email, String password);
    void modelOnLoginSuccess();
    void modelOnLoginError();
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void modelOnUserAlreadyLoggedIn();
    void modelOnFieldEmailEmpty();
    void modelOnFieldPasswordEmpty();
}
