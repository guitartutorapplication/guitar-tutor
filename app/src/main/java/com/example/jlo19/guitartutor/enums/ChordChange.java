package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating all different types of chord change speeds
 */

public enum ChordChange {
    // value = number of beats between chord changes
    ONE_BEAT(1),
    TWO_BEATS(2),
    FOUR_BEATS(4),
    EIGHT_BEATS(8),
    SIXTEEN_BEATS(16);

    private final int value;

    ChordChange(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
