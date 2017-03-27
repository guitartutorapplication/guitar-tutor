package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Response object from POST users request
 */
public class RegisterResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public RegisterResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
