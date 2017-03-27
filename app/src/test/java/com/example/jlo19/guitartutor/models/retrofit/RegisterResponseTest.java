package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing RegisterResponse
 */
public class RegisterResponseTest {
    @Test
    public void getMessage_ReturnsMessage() {
        // arrange
        String expectedMessage = "User has been successfully registered";
        RegisterResponse registerResponse = new RegisterResponse(false,
                expectedMessage);

        // act
        String actualMessage = registerResponse.getMessage();

        // assert
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
