package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.enums.ValidationError;

import java.util.List;

/**
 * Listener for RegisterInteractor
 */
public interface RegisterListener {
    void onRegisterSuccess();
    void onRegisterError();
    void onValidationFailed(List<ValidationError> validationErrors);
}
