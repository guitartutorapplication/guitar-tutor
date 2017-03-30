package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing PostPutResponse
 */
public class PostPutResponseTest {
    @Test
    public void getMessage_ReturnsMessage() {
        // arrange
        String expectedMessage = "User has been successfully registered";
        PostPutResponse postPutResponse = new PostPutResponse(false,
                expectedMessage);

        // act
        String actualMessage = postPutResponse.getMessage();

        // assert
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
