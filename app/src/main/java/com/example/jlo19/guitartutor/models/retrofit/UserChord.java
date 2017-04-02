package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Object that stores detail of a user chord
 */
public class UserChord {
    @SerializedName("chord_id")
    private int chordId;

    public UserChord(int chordId) {
        this.chordId = chordId;
    }

    public int getChordId() {
        return chordId;
    }
}
