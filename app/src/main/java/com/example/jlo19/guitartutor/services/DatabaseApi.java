package com.example.jlo19.guitartutor.services;

import com.example.jlo19.guitartutor.models.ChordsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Defines database API calls
 */
public interface DatabaseApi {
    @GET("chords")
    Call<ChordsResponse> getChords();
}
