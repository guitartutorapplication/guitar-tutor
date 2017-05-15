package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.EditAccountDetailsListener;

/**
 * Interface for EditAccountPresenter
 */
public interface IEditAccountPresenter extends IPresenter, EditAccountDetailsListener {
    void viewOnSave(String name, String email, String confirmEmail, String password, String confirmPassword);
}
