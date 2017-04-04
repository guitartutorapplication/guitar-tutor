package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Response object from POST /users/login requests
 */
public class LoginResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("user_id")
    private int userId;

    public LoginResponse(Boolean error, String message, int userId) {
        this.error = error;
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {return userId;}
}