package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.LoginListener;

/**
 * Interface for LoginPresenter
 */
public interface ILoginPresenter extends IPresenter, LoginListener {
    void viewOnRegister();
    void viewOnLogin(String email, String password);
}
