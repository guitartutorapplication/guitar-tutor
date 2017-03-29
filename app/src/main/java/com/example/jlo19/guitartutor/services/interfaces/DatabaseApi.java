package com.example.jlo19.guitartutor.services.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.LoginResponse;
import com.example.jlo19.guitartutor.models.retrofit.PostResponse;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.models.retrofit.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    @GET("users/{id}")
    Call<User> getAccountDetails(@Path("id") int userId);
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password);
}
