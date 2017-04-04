package com.example.jlo19.guitartutor.models.retrofit;

import android.os.Parcel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

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
    private String contents;

    @Before
    public void setUp() {
        title = "Adventure of a Lifetime";
        artist = "Coldplay";
        contents = "contents";
        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        song = new Song(title, artist, contents, chords);
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

    @Test
    public void describeContents_ReturnsZero() {
        // act
        int actual = song.describeContents();

        // assert
        Assert.assertEquals(0, actual);
    }

    @Test
    public void writeToParcel_WritesFieldsToParcel() {
        // arrange
        Parcel parcel = PowerMockito.mock(Parcel.class);

        // act
        song.writeToParcel(parcel, 0);

        // assert
        Mockito.verify(parcel).writeString(title);
        Mockito.verify(parcel).writeString(artist);
        Mockito.verify(parcel).writeString(contents);
        Mockito.verify(parcel).writeList(chords);
    }

    @Test
    public void newArray_ReturnsArrayOfSongsWithSpecifiedSize() {
        // arrange
        int expected = 1;

        // act
        Song[] songs = Song.CREATOR.newArray(expected);

        // assert
        Assert.assertEquals(expected, songs.length);
    }

    @Test
    public void getContents_ReturnsContents() {
        // act
        String actualContents = song.getContents();

        // assert
        Assert.assertEquals(contents, actualContents);
    }
}
