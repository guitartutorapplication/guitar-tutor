package com.example.jlo19.guitartutor.application;

import android.content.SharedPreferences;

/**
 * Wrapper for SharedPreferences
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.remove("api_key");
        editor.apply();
    }

    public void login(int userId, String apiKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.putString("api_key", apiKey);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != 0;
    }
}
