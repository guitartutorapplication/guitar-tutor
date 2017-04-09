package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.retrofit.objects.User;

/**
 * View interface for AccountActivity
 */
public interface AccountView extends IProgressBarView {
    void hideAccountButton();
    void setAccountDetails(User user);
    void showError();
    void startLoginActivity();
    void startEditAccountActivity();
}
