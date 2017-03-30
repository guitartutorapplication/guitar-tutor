package com.example.jlo19.guitartutor.models.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;

/**
 * Interface for EditAccountModel
 */
public interface IEditAccountModel {
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void setPresenter(IEditAccountPresenter presenter);
    void save(String name, String email, String confirmEmail, String password, String confirmPassword);
}
