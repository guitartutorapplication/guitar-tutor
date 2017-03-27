package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating different error messages from registering
 */
public enum RegisterError {
    // value = error message
    INVALID_EMAIL("Invalid email address"),
    ALREADY_REGISTERED("The specified email address is already registered");

    private final String value;

    RegisterError(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
