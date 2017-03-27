package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;

/**
 * Interface for LoginModel
 */
public interface ILoginModel {
    void setPresenter(ILoginPresenter presenter);
    void login(String email, String password);
}
