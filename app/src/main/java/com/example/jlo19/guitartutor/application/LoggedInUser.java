package com.example.jlo19.guitartutor.application;

import android.content.SharedPreferences;

/**
 * Wrapper for SharedPreferences to retrieve details about logged in user
 */
public class LoggedInUser {

    private final SharedPreferences sharedPreferences;

    public LoggedInUser(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public int getUserId() {
        return sharedPreferences.getInt("user_id", 0);
    }

    public String getApiKey() {
        return sharedPreferences.getString("api_key", "");
    }

    public void logout() {
        // removing details from shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.remove("api_key");
        editor.apply();
    }

    public void login(int userId, String apiKey) {
        // adding details to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.putString("api_key", apiKey);
        editor.apply();
    }

    public boolean isLoggedIn() {
        // user is logged in if details are present in shared preferences
        return getUserId() != 0;
    }
}
