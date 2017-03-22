package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Object that stores details of a song
 */
public class Song {
    @SerializedName("title")
    private String title;
    @SerializedName("artist")
    private String artist;
    @SerializedName("contents")
    private String contents;
    @SerializedName("chords")
    private List<Chord> chords;

    public Song(String title, String artist, String contents, List<Chord> chords) {
        this.title = title;
        this.artist = artist;
        this.contents = contents;
        this.chords = chords;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public List<Chord> getChords() {
        return chords;
    }
}
