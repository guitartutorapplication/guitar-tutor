package com.example.jlo19.guitartutor.services.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.PostResponse;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Defines database API calls
 */
public interface DatabaseApi {
    @GET("chords")
    Call<ChordsResponse> getChords();
    @GET("songs")
    Call<SongsResponse> getSongs();
    @FormUrlEncoded
    @POST("users")
    Call<PostResponse> registerUser(@Field("name") String name, @Field("email") String email,
                                    @Field("password") String password);
    @FormUrlEncoded
    @POST("users/login")
    Call<PostResponse> loginUser(@Field("email") String email, @Field("password") String password);
}
