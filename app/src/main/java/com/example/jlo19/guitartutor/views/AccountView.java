package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.retrofit.User;

/**
 * View interface for AccountActivity
 */
public interface AccountView extends IView {
    void hideAccountButton();
    void setAccountDetails(User user);
    void showError();
    void showProgressBar();
    void hideProgressBar();
}
