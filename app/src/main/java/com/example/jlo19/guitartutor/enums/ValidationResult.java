package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating different results of data validation on account details
 */
public enum ValidationResult {
    FIELD_EMPTY,
    EMAIL_MISMATCH,
    PASSWORD_MISMATCH,
    INVALID_EMAIL,
    PASSWORD_TOO_SHORT,
    PASSWORD_NO_UPPER,
    PASSWORD_NO_LOWER,
    PASSWORD_NO_NUMBER,
    VALID_DATA
}
