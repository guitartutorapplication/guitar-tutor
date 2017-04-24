package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.enums.ValidationError;

import java.util.List;

/**
 * Interface for RegisterPresenter
 */
public interface IRegisterPresenter extends IPresenter {
    void viewOnRegister(String name, String email, String confirmEmail, String password, String confirmPassword);
    void modelOnRegisterError();
    void modelOnRegisterSuccess();
    void modelOnValidationFailed(List<ValidationError> errors);
}
