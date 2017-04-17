package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.ValidationResult;

import java.util.regex.Pattern;

/**
 * An abstract class which deals with the data validation for an account
 */
class DataValidationModel {
    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    static ValidationResult validate(String name, String email, String confirmEmail, String password,
                                     String confirmPassword) {
        if (name.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty()) {
            return ValidationResult.FIELD_EMPTY;
        }
        else if (!email.equals(confirmEmail)) {
            return ValidationResult.EMAIL_MISMATCH;
        }
        else if(!password.equals(confirmPassword)) {
            return ValidationResult.PASSWORD_MISMATCH;
        }
        else if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return ValidationResult.INVALID_EMAIL;
        }
        else if(password.length() < 8) {
            return ValidationResult.PASSWORD_TOO_SHORT;
        }
        // checks if password contains upper case letter
        else if(password.equals(password.toLowerCase())) {
            return ValidationResult.PASSWORD_NO_UPPER;
        }
        // checks if password contains lower case letter
        else if(password.equals(password.toUpperCase())) {
            return ValidationResult.PASSWORD_NO_LOWER;
        }
        // checks if password contains number
        else if(!password.matches(".*\\d+.*")) {
            return ValidationResult.PASSWORD_NO_NUMBER;
        }
        else {
            return ValidationResult.VALID_DATA;
        }
    }

    static ValidationResult validateResponse(String responseMessage) {
        if (responseMessage.equals(ValidationResult.INVALID_EMAIL.toString())) {
            return ValidationResult.INVALID_EMAIL;
        }
        else if (responseMessage.equals(ValidationResult.EMAIL_ALREADY_REGISTERED.toString())) {
            return ValidationResult.EMAIL_ALREADY_REGISTERED;
        }
        else if (responseMessage.equals(ValidationResult.PASSWORD_TOO_SHORT.toString())) {
            return ValidationResult.PASSWORD_TOO_SHORT;
        }
        else if (responseMessage.equals(ValidationResult.PASSWORD_NO_UPPER.toString())) {
            return ValidationResult.PASSWORD_NO_UPPER;
        }
        else if (responseMessage.equals(ValidationResult.PASSWORD_NO_LOWER.toString())) {
            return ValidationResult.PASSWORD_NO_LOWER;
        }
        else if (responseMessage.equals(ValidationResult.PASSWORD_NO_NUMBER.toString())) {
            return ValidationResult.PASSWORD_NO_NUMBER;
        }
        else {
            return ValidationResult.VALID_DATA;
        }
    }
}
