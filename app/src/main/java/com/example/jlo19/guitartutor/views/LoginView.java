package com.example.jlo19.guitartutor.views;

/**
 * View interface for LoginActivity
 */
public interface LoginView extends ProgressBarView {
    void startRegisterActivity();
    void showFieldEmailEmptyError();
    void startHomeActivity();
    void showLoginError();
    void showFieldPasswordEmptyError();
    void resetFieldEmptyErrors();
}
