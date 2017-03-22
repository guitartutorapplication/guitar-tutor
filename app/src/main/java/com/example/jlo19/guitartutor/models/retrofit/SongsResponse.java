package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response object from GET songs request
 */
public class SongsResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("songs")
    private List<Song> songs;

    public SongsResponse(boolean error, List<Song> songs) {
        this.error = error;
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }
}
