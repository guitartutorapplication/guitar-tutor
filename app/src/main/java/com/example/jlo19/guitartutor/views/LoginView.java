package com.example.jlo19.guitartutor.views;

/**
 * View interface for LoginActivity
 */
public interface LoginView extends IView {
    void startRegisterActivity();
    void showProgressBar();
    void showFieldEmptyError();
    void hideProgressBar();
    void startHomeActivity();
    void showIncorrectCredentialsError();
    void showLoginError();
}
