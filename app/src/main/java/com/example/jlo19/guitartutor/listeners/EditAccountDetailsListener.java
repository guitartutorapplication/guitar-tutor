package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.enums.ValidationError;
import java.util.List;

/**
 * Listener for EditAccountDetailsInteractor
 */
public interface EditAccountDetailsListener {
    void onSaveSuccess();
    void onSaveError();
    void onValidationFailed(List<ValidationError> validationErrors);
}
