package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating different results of data validation on account details
 */
public enum ValidationResult {
    // value = error message from API when this is the validation result
    FIELD_EMPTY(""),
    EMAIL_MISMATCH(""),
    PASSWORD_MISMATCH(""),
    INVALID_EMAIL("Invalid email address"),
    EMAIL_ALREADY_REGISTERED("The specified email address is already registered"),
    PASSWORD_TOO_SHORT("Password must be at least 8 characters in length"),
    PASSWORD_NO_UPPER("Password must have at least one upper case letter"),
    PASSWORD_NO_LOWER("Password must have at least one lower case letter"),
    PASSWORD_NO_NUMBER("Password must have at least one number"),
    VALID_DATA("");

    private final String value;

    ValidationResult(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
