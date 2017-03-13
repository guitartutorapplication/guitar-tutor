package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response object from GET chords request
 */
public class ChordsResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("chords")
    private List<Chord> chords;

    public ChordsResponse(boolean error, List<Chord> chords) {
        this.error = error;
        this.chords = chords;
    }

    public List<Chord> getChords() {
        return chords;
    }
}
