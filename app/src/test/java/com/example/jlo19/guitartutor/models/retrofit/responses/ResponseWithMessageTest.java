package com.example.jlo19.guitartutor.models.retrofit.responses;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing ResponseWithMessage
 */
public class ResponseWithMessageTest {
    @Test
    public void getMessage_ReturnsMessage() {
        // arrange
        String expectedMessage = "Invalid email address";
        ResponseWithMessage responseWithMessage = new ResponseWithMessage(
                expectedMessage);

        // act
        String actualMessage = responseWithMessage.getMessage();

        // assert
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
