package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Testing UserChordsResponse
 */
public class UserChordsResponseTest {
    @Test
    public void getUserChords_ReturnsUserChords() {
        // arrange
        List<UserChord> userChords = Arrays.asList(new UserChord(1),
                new UserChord(2));
        UserChordsResponse userChordsResponse = new UserChordsResponse(userChords);

        // act
        List<UserChord> actualUserChords = userChordsResponse.getUserChords();
        Assert.assertEquals(userChords, actualUserChords);
    }
}
