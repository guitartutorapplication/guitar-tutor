package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Response object from general POST/PUT requests
 */
public class PostPutResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public PostPutResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
