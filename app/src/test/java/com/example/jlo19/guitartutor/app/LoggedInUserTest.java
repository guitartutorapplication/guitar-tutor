package com.example.jlo19.guitartutor.app;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.LoggedInUser;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Testing LoggedInUser
 */
public class LoggedInUserTest {
    private SharedPreferences sharedPreferences;
    private LoggedInUser loggedInUser;
    private int userId;
    private String apiKey;

    @Before
    public void setUp() {
        userId = 2;
        apiKey = "api_key";

        sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        Mockito.when(sharedPreferences.getString("api_key", "")).thenReturn(apiKey);
        loggedInUser = new LoggedInUser(sharedPreferences);
    }

    @Test
    public void getUserId_ReturnsUserIdFromSharedPreferences() {
        // act
        int actualUserId = loggedInUser.getUserId();

        // assert
        Assert.assertEquals(userId, actualUserId);
    }

    @Test
    public void getApiKey_ReturnsApiKeyFromSharedPreferences() {
        // act
        String actualApiKey = loggedInUser.getApiKey();

        // assert
        Assert.assertEquals(apiKey, actualApiKey);
    }

    @Test
    public void logout_RemovesUserIdAndApiKeyFromSharedPreferences() {
        // arrange
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);

        // act
        loggedInUser.logout();

        // assert
        Mockito.verify(editor).remove("user_id");
        Mockito.verify(editor).remove("api_key");
        Mockito.verify(editor).apply();
    }

    @Test
    public void login_AddsUserIdAndApiKeyToSharedPreferences() {
        // arrange
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);

        // act
        int userId = 5;
        String apiKey = "apikey";
        loggedInUser.login(userId, apiKey);

        // assert
        Mockito.verify(editor).putInt("user_id", userId);
        Mockito.verify(editor).putString("api_key", apiKey);
        Mockito.verify(editor).apply();
    }

    @Test
    public void withUserIdInSharedPreferences_IsLoggedIn_ReturnsTrue() {
        // act
        boolean isLoggedIn = loggedInUser.isLoggedIn();

        // assert
        Assert.assertTrue(isLoggedIn);
    }

    @Test
    public void withNoUserIdInSharedPreferences_IsLoggedIn_ReturnsFalse() {
        // arrange
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(0);

        // act
        boolean isLoggedIn = loggedInUser.isLoggedIn();

        // assert
        Assert.assertFalse(isLoggedIn);
    }
}
