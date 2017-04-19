package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class LoginModel implements ILoginModel {

    private ILoginPresenter presenter;
    private DatabaseApi api;
    private SharedPreferences sharedPreferences;

    public LoginModel() {
        App.getComponent().inject(this);}

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(ILoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            presenter.modelOnFieldEmpty();
        }
        else {
            Call<User> call = api.loginUser(email, password);

            // asynchronously executing call
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        // adding user id and api key to shared preferences for later use
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("user_id", response.body().getId());
                        editor.putString("api_key", response.body().getApiKey());
                        editor.apply();

                        presenter.modelOnLoginSuccess();
                    }
                    else {
                        presenter.modelOnLoginError();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    presenter.modelOnLoginError();
                }
            });
        }
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void checkForPreexistingLogIn() {
        // retrieving logged in user's id (if any) from shared preferences
        int userId = sharedPreferences.getInt("user_id", 0);

        if (userId != 0) {
            presenter.modelOnUserAlreadyLoggedIn();
        }
    }
}
