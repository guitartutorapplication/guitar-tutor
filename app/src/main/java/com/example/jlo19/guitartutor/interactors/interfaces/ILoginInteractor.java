package com.example.jlo19.guitartutor.interactors.interfaces;

import com.example.jlo19.guitartutor.listeners.LoginListener;

/**
 * Interface for LoginInteractor
 */
public interface ILoginInteractor {
    void setListener(LoginListener listener);
    void login(String email, String password);
}
