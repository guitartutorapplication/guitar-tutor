package com.example.jlo19.guitartutor.enums;

/**
 * Enum demonstrating the different states within the practise activity
 */
public enum PractiseActivityState {
    // filename is the sound to be played when the activity enters that state
    // message is what is displayed on the screen when the activity enters that state
    COUNTDOWN_STAGE_3("countdown_3", "3"),
    COUNTDOWN_STAGE_2("countdown_2", "2"),
    COUNTDOWN_STAGE_1("countdown_1", "1"),
    COUNTDOWN_STAGE_GO("countdown_go", "GO!"),
    // no change to what appears on screen when a new beat
    NEW_BEAT("metronome_sound"),
    // filename and message is determined by the individual chord
    NEW_CHORD();

    private String filename;
    private String message;

    PractiseActivityState() {
    }

    PractiseActivityState(String filename) {
        this.filename = filename;
    }

    PractiseActivityState(String filename, String message) {
        this.filename = filename;
        this.message = message;
    }

    public String getFilename() {
        return filename;
    }

    public String toString() {
        return message;
    }
}
