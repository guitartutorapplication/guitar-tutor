package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.IAccountActivityModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to DB API
 */
public class AccountActivityModel implements IAccountActivityModel {

    private DatabaseApi api;
    private IAccountActivityPresenter presenter;
    private SharedPreferences sharedPreferences;

    public AccountActivityModel() {
        App.getComponent().inject(this);
    }

    @Inject
    public void setApi(DatabaseApi api) {this.api = api;}

    @Override
    public void setPresenter(IAccountActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void getAccountActivity() {
        // retrieving logged in user's id & api key from shared preferences
        int userId = sharedPreferences.getInt("user_id", 0);
        String apiKey = sharedPreferences.getString("api_key", "");

        Call<List<Chord>> call = api.getUserChords(apiKey, userId);
        // asynchronously executing call
        call.enqueue(new Callback<List<Chord>>() {
            @Override
            public void onResponse(Call<List<Chord>> call, Response<List<Chord>> response) {
                if (response.isSuccessful()) {
                    presenter.modelOnAccountActivityRetrieved(response.body());
                }
                else {
                    presenter.modelOnError();
                }
            }

            @Override
            public void onFailure(Call<List<Chord>> call, Throwable t) {
                presenter.modelOnError();
            }
        });
    }
}
