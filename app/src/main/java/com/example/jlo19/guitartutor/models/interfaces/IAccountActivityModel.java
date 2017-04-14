package com.example.jlo19.guitartutor.models.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;

/**
 * Interface for AccountActivityModel
 */
public interface IAccountActivityModel {
    void setPresenter(IAccountActivityPresenter presenter);
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void getAccountActivity();
}
