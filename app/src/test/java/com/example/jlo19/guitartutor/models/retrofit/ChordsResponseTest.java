package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Testing ChordsResponse
 */

public class ChordsResponseTest {
    @Test
    public void getChords_ReturnsChords() {
        // arrange
        List<Chord> expectedChords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        ChordsResponse response = new ChordsResponse(false, expectedChords);

        // act
        List<Chord> actualChords = response.getChords();

        // assert
        Assert.assertEquals(expectedChords, actualChords);
    }
}
