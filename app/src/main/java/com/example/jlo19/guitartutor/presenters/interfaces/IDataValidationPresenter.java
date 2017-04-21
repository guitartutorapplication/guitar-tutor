package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.enums.ValidationError;

import java.util.List;

/**
 * Represents common data validation behaviour
 */
interface IDataValidationPresenter extends IPresenter  {
    void modelOnValidationFailed(List<ValidationError> errors);
}
