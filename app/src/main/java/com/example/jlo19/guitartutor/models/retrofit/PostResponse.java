package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Response object from general POST requests
 */
public class PostResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public PostResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
