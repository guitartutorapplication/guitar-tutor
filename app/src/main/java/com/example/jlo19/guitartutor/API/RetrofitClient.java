package com.example.jlo19.guitartutor.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit support
 */

public class RetrofitClient {

    private static final String BASE_URL = "http://ec2-34-251-98-53.eu-west-1.compute.amazonaws.com/guitartutor/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
