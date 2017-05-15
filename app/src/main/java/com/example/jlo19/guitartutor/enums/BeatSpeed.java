package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating all different types of beat speeds
 */
public enum BeatSpeed {
    // value = milliseconds between beats
    VERY_SLOW(1500),
    SLOW(1250),
    MEDIUM(1000),
    FAST(750),
    VERY_FAST(500);

    private final int value;

    BeatSpeed(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
