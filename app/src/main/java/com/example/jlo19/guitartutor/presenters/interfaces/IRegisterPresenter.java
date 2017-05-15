package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.RegisterListener;

/**
 * Interface for RegisterPresenter
 */
public interface IRegisterPresenter extends IPresenter, RegisterListener {
    void viewOnRegister(String name, String email, String confirmEmail, String password, String confirmPassword);
}
