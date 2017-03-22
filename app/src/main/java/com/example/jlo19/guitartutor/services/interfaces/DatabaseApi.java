package com.example.jlo19.guitartutor.services.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Defines database API calls
 */
public interface DatabaseApi {
    @GET("chords")
    Call<ChordsResponse> getChords();
    @GET("songs")
    Call<SongsResponse> getSongs();
}
