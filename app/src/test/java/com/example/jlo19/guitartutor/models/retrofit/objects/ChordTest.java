package com.example.jlo19.guitartutor.models.retrofit.objects;

import android.os.Build;
import android.os.Parcel;

import com.example.jlo19.guitartutor.BuildConfig;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


/**
 * Testing Chord
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class ChordTest {

    @Test
    public void getDiagramFilename_ReturnsDiagramFilename() {
        // arrange
        String expected = "A.png";
        Chord chord = new Chord(1, "A", "MAJOR", expected, "A.mp4", 1);

        // act
        String actual = chord.getDiagramFilename();

        // assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getId_ReturnsId() {
        // arrange
        int expected = 1;
        Chord chord = new Chord(expected, "A", "MAJOR", "A.png", "A.mp4", 1);

        // act
        int actual = chord.getId();

        // assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void majorChord_ToString_ReturnsName() {
        // arrange
        Chord chord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1);

        // act
        String actual = chord.toString();

        // assert
        Assert.assertEquals("A", actual);
    }

    @Test
    public void minorChord_ToString_ReturnsNameWithm() {
        // arrange
        Chord chord = new Chord(1, "A", "MINOR", "Am.png", "Am.mp4", 1);

        // act
        String actual = chord.toString();

        // assert
        Assert.assertEquals("Am", actual);
    }

    @Test
    public void sevenChord_ToString_ReturnsNameWith7() {
        // arrange
        Chord chord = new Chord(1, "A", "SEVEN", "A7.png", "A7.mp4", 1);

        // act
        String actual = chord.toString();

        // assert
        Assert.assertEquals("A7", actual);
    }

    @Test
    public void sharpChord_ToString_ReturnsNameWithSharpSymbol() {
        // arrange
        Chord chord = new Chord(1, "A", "SHARP", "A sharp.png", "A sharp.mp4", 1);

        // act
        String actual = chord.toString();

        // assert
        Assert.assertEquals("A#", actual);
    }

    @Test
    public void sharpMinorChord_ToString_ReturnsNameWithSharpSymbolAndM() {
        // arrange
        Chord chord = new Chord(1, "A", "SHARP_MINOR", "A sharp m.png", "A sharp m.mp4", 1);

        // act
        String actual = chord.toString();

        // assert
        Assert.assertEquals("A#m", actual);
    }

    @Test
    public void flatChord_ToString_ReturnsNameWithFlatSymbol() {
        // arrange
        Chord chord = new Chord(1, "A", "FLAT", "A flat.png", "A flat.mp4", 1);

        // act
        String actual = chord.toString();

        // assert
        Assert.assertEquals("A♭", actual);
    }

    @Test
    public void unrecognisedChord_ToString_ReturnsName() {
        // arrange
        Chord chord = new Chord(1, "A", "MINOR_SEVEN", "Am7.png", "Am7.mp4", 1);

        // act
        String actual = chord.toString();

        // assert
        Assert.assertEquals("A", actual);
    }

    @Test
    public void describeContents_ReturnsZero() {
        // arrange
        Chord chord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1);

        // act
        int actual = chord.describeContents();

        // assert
        Assert.assertEquals(0, actual);
    }

    @Test
    public void writeToParcel_WritesFieldsToParcel() {
        // arrange
        Chord chord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1);
        Parcel parcel = PowerMockito.mock(Parcel.class);

        // act
        chord.writeToParcel(parcel, 0);

        // assert
        Mockito.verify(parcel).writeInt(1);
        Mockito.verify(parcel).writeString("A");
        Mockito.verify(parcel).writeString("MAJOR");
        Mockito.verify(parcel).writeString("A.png");
    }

    @Test
    public void newArray_ReturnsArrayOfChordsWithSpecifiedSize() {
        // arrange
        int expected = 1;

        // act
        Chord[] chords = Chord.CREATOR.newArray(expected);

        // assert
        Assert.assertEquals(expected, chords.length);
    }

    @Test
    public void getLevelRequired_ReturnsLevelRequired() {
        // act
        int level = 1;
        Chord chord = new Chord(1, "A", "MINOR_SEVEN", "Am7.png", "Am7.mp4", level);
        int actualLevel = chord.getLevelRequired();

        // assert
        Assert.assertEquals(level, actualLevel);
    }
}
