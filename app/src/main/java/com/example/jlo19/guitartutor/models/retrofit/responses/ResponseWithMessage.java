package com.example.jlo19.guitartutor.models.retrofit.responses;

import com.google.gson.annotations.SerializedName;

/**
 * For responses that return a message (either error or success)
 */
public class ResponseWithMessage {
    @SerializedName("message")
    private final String message;

    public ResponseWithMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
