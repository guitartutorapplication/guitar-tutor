package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Testing Song
 */
public class SongTest {
    private Song song;
    private String title;
    private String artist;
    private List<Chord> chords;

    @Before
    public void setUp() {
        title = "Adventure of a Lifetime";
        artist = "Coldplay";
        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        song = new Song(title, artist, "contents", chords);
    }

    @Test
    public void getTitle_ReturnsTitle() {
        // act
        String actualTitle = song.getTitle();

        // assert
        Assert.assertEquals(title, actualTitle);
    }

    @Test
    public void getArtist_ReturnsArtist() {
        // act
        String actualArtist = song.getArtist();

        // assert
        Assert.assertEquals(artist, actualArtist);
    }

    @Test
    public void getChords_ReturnsChords() {
        // act
        List<Chord> actualChords = song.getChords();

        // assert
        Assert.assertEquals(chords, actualChords);
    }
}
