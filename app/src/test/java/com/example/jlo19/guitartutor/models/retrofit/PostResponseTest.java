package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing PostResponse
 */
public class PostResponseTest {
    @Test
    public void getMessage_ReturnsMessage() {
        // arrange
        String expectedMessage = "User has been successfully registered";
        PostResponse postResponse = new PostResponse(false,
                expectedMessage);

        // act
        String actualMessage = postResponse.getMessage();

        // assert
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
