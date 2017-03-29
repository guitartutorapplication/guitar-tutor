package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.models.retrofit.User;

/**
 * Interface for AccountPresenter
 */
public interface IAccountPresenter extends IPresenter {
    void setSharedPreferences(SharedPreferences preferences);
    void modelOnAccountDetailsRetrieved(User user);
    void modelOnError();
}
