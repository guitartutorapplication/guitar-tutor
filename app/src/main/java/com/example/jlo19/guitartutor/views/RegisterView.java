package com.example.jlo19.guitartutor.views;

/**
 * View interface for RegisterActivity
 */

public interface RegisterView extends IDataValidationView {
    void showRegisterError();
    void showAlreadyRegisteredError();
    void startLoginActivity();
}
