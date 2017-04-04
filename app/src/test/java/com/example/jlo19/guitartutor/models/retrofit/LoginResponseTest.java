package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing LoginResponse
 */
public class LoginResponseTest {
    @Test
    public void getMessage_ReturnsMessage() {
        // arrange
        String expectedMessage = "Login successful.";
        LoginResponse loginResponse = new LoginResponse(false,
                expectedMessage, 1);

        // act
        String actualMessage = loginResponse.getMessage();

        // assert
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getUserId_ReturnsUserId() {
        // arrange
        int expectedUserId = 1;
        LoginResponse loginResponse = new LoginResponse(false, "Login successful.", expectedUserId);

        // act
        int actualUserId = loginResponse.getUserId();

        // assert
        Assert.assertEquals(expectedUserId, actualUserId);
    }
}
