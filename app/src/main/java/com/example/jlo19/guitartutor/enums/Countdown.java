package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating different countdown states
 */

public enum Countdown {
    // value = string value
    THREE("3"),
    TWO("2"),
    ONE("1"),
    GO("GO!");

    private final String value;

    Countdown(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
