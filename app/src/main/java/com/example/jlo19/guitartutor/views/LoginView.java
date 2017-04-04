package com.example.jlo19.guitartutor.views;

/**
 * View interface for LoginActivity
 */
public interface LoginView extends IProgressBarView {
    void startRegisterActivity();
    void showFieldEmptyError();
    void startHomeActivity();
    void showIncorrectCredentialsError();
    void showLoginError();
}
