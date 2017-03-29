package com.example.jlo19.guitartutor.models.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;

/**
 * Interface for AccountModel
 */
public interface IAccountModel {
    void setPresenter(IAccountPresenter presenter);
    void setSharedPreferences(SharedPreferences sharedPreferences);
    void getAccountDetails();
}
