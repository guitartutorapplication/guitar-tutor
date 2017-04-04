package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating different error messages from API
 */
public enum ResponseError {
    // value = error message
    INVALID_EMAIL("Invalid email address"),
    ALREADY_REGISTERED("The specified email address is already registered"),
    INCORRECT_CREDENTIALS("Login failed. Incorrect email or password.");

    private final String value;

    ResponseError(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
