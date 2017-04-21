package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.ValidationError;

import java.util.ArrayList;
import java.util.List;
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

    static List<ValidationError> validate(String name, String email, String confirmEmail, String password,
                                          String confirmPassword) {
        List<ValidationError> errors = new ArrayList<>();

        // checks if password contains number
        if(!password.matches(".*\\d+.*")) {
            errors.add(ValidationError.PASSWORD_NO_NUMBER);
        }
        // checks if password contains lower case letter
        if (password.equals(password.toUpperCase())) {
            errors.add(ValidationError.PASSWORD_NO_LOWER);
        }
        // checks if password contains upper case letter
        if (password.equals(password.toLowerCase())) {
            errors.add(ValidationError.PASSWORD_NO_UPPER);
        }
        if (password.length() < 8) {
            errors.add(ValidationError.PASSWORD_TOO_SHORT);
        }
        if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            errors.add(ValidationError.INVALID_EMAIL);
        }
        if (!password.equals(confirmPassword)) {
            errors.add(ValidationError.PASSWORD_MISMATCH);
        }
        if (!email.equals(confirmEmail)) {
            errors.add(ValidationError.EMAIL_MISMATCH);
        }
        if (confirmPassword.isEmpty()) {
            errors.add(ValidationError.FIELD_EMPTY_CONFIRM_PASSWORD);
        }
        if (password.isEmpty()) {
            errors.add(ValidationError.FIELD_EMPTY_PASSWORD);
        }
        if (confirmEmail.isEmpty()) {
            errors.add(ValidationError.FIELD_EMPTY_CONFIRM_EMAIL);
        }
        if (email.isEmpty()) {
            errors.add(ValidationError.FIELD_EMPTY_EMAIL);
        }
        if (name.isEmpty()) {
            errors.add(ValidationError.FIELD_EMPTY_NAME);
        }
        return errors;
    }

    static List<ValidationError> validateResponse(String responseMessage) {
        // converting raw response to a list of string errors
        responseMessage = responseMessage.replace("[", "").replace("]", "").replace("\"", "");
        String[] errors = responseMessage.split(",");

        List<ValidationError> validationErrors = new ArrayList<>();

        for (String error : errors) {
            if (error.equals(ValidationError.INVALID_EMAIL.toString())) {
                validationErrors.add(ValidationError.INVALID_EMAIL);
            }
            else if (error.equals(ValidationError.EMAIL_ALREADY_REGISTERED.toString())) {
                validationErrors.add(ValidationError.EMAIL_ALREADY_REGISTERED);
            }
            else if (error.equals(ValidationError.PASSWORD_TOO_SHORT.toString())) {
                validationErrors.add(ValidationError.PASSWORD_TOO_SHORT);
            }
            else if (error.equals(ValidationError.PASSWORD_NO_UPPER.toString())) {
                validationErrors.add(ValidationError.PASSWORD_NO_UPPER);
            }
            else if (error.equals(ValidationError.PASSWORD_NO_LOWER.toString())) {
                validationErrors.add(ValidationError.PASSWORD_NO_LOWER);
            }
            else if (error.equals(ValidationError.PASSWORD_NO_NUMBER.toString())) {
                validationErrors.add(ValidationError.PASSWORD_NO_NUMBER);
            }
        }

        return validationErrors;
    }
}
