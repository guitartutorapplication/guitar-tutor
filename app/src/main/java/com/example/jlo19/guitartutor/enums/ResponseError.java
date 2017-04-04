package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating different error messages from API
 */
public enum ResponseError {
    // value = error message
    INVALID_EMAIL("Invalid email address"),
    ALREADY_REGISTERED("The specified email address is already registered"),
    INCORRECT_CREDENTIALS("Login failed. Incorrect email or password."),
    RETRIEVE_LEVEL_DETAILS_FAILURE("An error occured while trying to retrieve user's level details."),
    UPDATE_LEVEL_DETAILS_FAILURE("An error occured while trying to update user's level details.");

    private final String value;

    ResponseError(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
