package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.IAccountModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to DB API
 */
public class AccountModel implements IAccountModel {

    private IAccountPresenter presenter;
    private DatabaseApi api;
    private SharedPreferences sharedPreferences;

    public AccountModel() {
        App.getComponent().inject(this);
    }

    @Inject
    public void setApi(DatabaseApi api) {this.api = api;}

    @Override
    public void setPresenter(IAccountPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void getAccountDetails() {
        // retrieving logged in user's id from shared preferences
        int userId = sharedPreferences.getInt("user_id", 0);

        Call<User> call = api.getAccountDetails(userId);

        // asynchronously executing call
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    presenter.modelOnAccountDetailsRetrieved(response.body());
                }
                else {
                    presenter.modelOnError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                presenter.modelOnError();
            }
        });
    }

    @Override
    public void logout() {
       // removing user id from shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.apply();
    }
}
