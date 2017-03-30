package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

/**
 * Interface for EditAccountPresenter
 */
public interface IEditAccountPresenter extends IDataValidationPresenter {
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void viewOnSave(String name, String email, String confirmEmail, String password, String confirmPassword);
    void modelOnSaveSuccess();
    void modelOnSaveError();
}
