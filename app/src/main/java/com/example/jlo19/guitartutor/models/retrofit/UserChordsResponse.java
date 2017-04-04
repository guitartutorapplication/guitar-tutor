package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response object from GET users/id/chords request
 */
public class UserChordsResponse {
    @SerializedName("user_chords")
    private List<UserChord> userChords;

    public UserChordsResponse(List<UserChord> userChords) {
        this.userChords = userChords;
    }

    public List<UserChord> getUserChords() {
        return userChords;
    }
}
