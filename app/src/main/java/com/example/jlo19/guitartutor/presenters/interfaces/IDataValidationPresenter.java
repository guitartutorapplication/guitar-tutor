package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.enums.ValidationResult;

/**
 * Represents common data validation behaviour
 */
interface IDataValidationPresenter extends IPresenter  {
    void modelOnValidationFailed(ValidationResult result);
}
