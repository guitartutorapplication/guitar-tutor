package com.example.jlo19.guitartutor.API;

import com.example.jlo19.guitartutor.model.ChordsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Defines API calls
 */

public interface GuitarTutorApi {
    @GET("chords")
    Call<ChordsResponse> getChords();
}
