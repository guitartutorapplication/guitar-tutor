package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ResponseError;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.models.retrofit.LoginResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

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
            Call<LoginResponse> call = api.loginUser(email, password);

            // asynchronously executing call
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        // adding user id to shared preferences for later use
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("user_id", response.body().getUserId());
                        editor.apply();

                        presenter.modelOnLoginSuccess();
                    }
                    else {
                        // convert raw response when error
                        Gson gson = new Gson();
                        TypeAdapter<LoginResponse> adapter = gson.getAdapter(LoginResponse.class);

                        try {
                            LoginResponse loginResponse = adapter.fromJson(
                                    response.errorBody().string());

                            if (loginResponse.getMessage().equals(
                                    ResponseError.INCORRECT_CREDENTIALS.toString())) {
                                presenter.modelOnIncorrectCredentials();
                            }
                            else {
                                presenter.modelOnLoginError();
                            }

                        } catch (IOException e) {
                            presenter.modelOnLoginError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
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
