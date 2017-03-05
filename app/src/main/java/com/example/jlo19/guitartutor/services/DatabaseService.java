package com.example.jlo19.guitartutor.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creates database API using retrofit
 */
public class DatabaseService {

    private static final String BASE_URL = "http://ec2-34-251-98-53.eu-west-1.compute.amazonaws.com/guitartutor/v1/";
    private static Retrofit retrofit = null;

    public static DatabaseApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(DatabaseApi.class);
    }
}
