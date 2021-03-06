package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.User;

/**
 * View interface for AccountActivity
 */
public interface AccountView extends ProgressBarView {
    void hideAccountButton();
    void setAccountDetails(String name, String email, int level, int achievements);
    void showError();
    void startLoginActivity();
    void startEditAccountActivity(User user);
    void startAccountActivityActivity();
    void finishActivity();
}
