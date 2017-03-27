package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;

/**
 * Interface for RegisterModel
 */
public interface IRegisterModel {
    void setPresenter(IRegisterPresenter presenter);
    void register(String name, String email, String confirmEmail, String password,
                  String confirmPassword);
}
