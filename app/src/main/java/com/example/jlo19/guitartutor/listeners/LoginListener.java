package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.retrofit.objects.User;

public interface LoginListener {
    void onLoginSuccess(User user);
    void onLoginError();
}
