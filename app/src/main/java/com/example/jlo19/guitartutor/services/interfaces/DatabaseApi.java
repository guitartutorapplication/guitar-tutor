package com.example.jlo19.guitartutor.services.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.LoginResponse;
import com.example.jlo19.guitartutor.models.retrofit.PostPutResponse;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.models.retrofit.User;
import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<PostPutResponse> registerUser(@Field("name") String name, @Field("email") String email,
                                       @Field("password") String password);
    @GET("users/{id}")
    Call<User> getAccountDetails(@Path("id") int userId);
    @FormUrlEncoded
    @PUT("users/{id}")
    Call<PostPutResponse> editAccountDetails(@Path("id") int userId, @Field("name") String name,
                                             @Field("email") String email, @Field("password")
                                                     String password);
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password);
    @GET("users/{id}/chords")
    Call<UserChordsResponse> getUserChords(@Path("id") int userId);
    @FormUrlEncoded
    @POST("users/{id}/chords")
    Call<PostPutResponse> addLearntChord(@Path("id") int userId, @Field("chord_id") int chordId);
}
