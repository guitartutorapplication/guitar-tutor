package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.User;

/**
 * Listener for LoginInteractor
 */
public interface LoginListener {
    void onLoginSuccess(User user);
    void onLoginError();
}
