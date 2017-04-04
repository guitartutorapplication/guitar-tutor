package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing UserChord
 */
public class UserChordTest {
    @Test
    public void getId_ReturnsId() {
        // arrange
        int chordId = 1;
        UserChord userChord = new UserChord(chordId);

        // act
        int actualChordId = userChord.getChordId();

        // assert
        Assert.assertEquals(chordId, actualChordId);
    }
}
