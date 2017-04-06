package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Testing SongsResponse
 */
public class SongsResponseTest {
    @Test
    public void getSongs_ReturnsSongs() {
        // arrange
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1));
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));

        SongsResponse response = new SongsResponse(false, songs);

        // act
        List<Song> actualSongs = response.getSongs();

        // assert
        Assert.assertEquals(songs, actualSongs);
    }
}
