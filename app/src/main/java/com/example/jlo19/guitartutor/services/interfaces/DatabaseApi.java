package com.example.jlo19.guitartutor.services.interfaces;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.models.retrofit.responses.ResponseWithMessage;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Defines database API calls
 */
public interface DatabaseApi {
    @GET("chords")
    Call<List<Chord>> getChords(@Header("Authorization") String apiKey);

    @GET("songs")
    Call<List<Song>> getSongs(@Header("Authorization") String apiKey);

    @FormUrlEncoded
    @POST("users")
    Call<ResponseWithMessage> registerUser(
            @Field("name") String name, @Field("email") String email, @Field("password") String password);

    @GET("users/{id}")
    Call<User> getAccountDetails(@Header("Authorization") String apiKey, @Path("id") int userId);
    @FormUrlEncoded
    @PUT("users/{id}")
    Call<ResponseWithMessage> editAccountDetails(@Header("Authorization") String apiKey, @Path("id")
            int userId, @Field("name") String name, @Field("email") String email, @Field("password")
            String password);

    @FormUrlEncoded
    @POST("users/login")
    Call<User> loginUser(@Field("email") String email, @Field("password") String password);

    @GET("users/{id}/chords")
    Call<List<Chord>> getUserChords(@Header("Authorization") String apiKey, @Path("id") int userId);
    @FormUrlEncoded
    @POST("users/{id}/chords")
    Call<User> addUserChord(@Header("Authorization") String apiKey, @Path("id") int userId,
                            @Field("chord_id") int chordId);
    @FormUrlEncoded
    @PUT("users/{id}/chords")
    Call<User> updateUserChords(@Header("Authorization") String apiKey, @Path("id") int userId,
                                @Field("chord_ids[]") ArrayList<Integer> chordIds);
}
