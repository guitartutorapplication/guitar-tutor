package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ResponseError;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.models.retrofit.PostResponse;
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
            Call<PostResponse> call = api.loginUser(email, password);

            // asynchronously executing call
            call.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if (response.isSuccessful()) {
                        presenter.modelOnLoginSuccess();
                    }
                    else {
                        // convert raw response when error
                        Gson gson = new Gson();
                        TypeAdapter<PostResponse> adapter = gson.getAdapter(PostResponse.class);

                        try {
                            PostResponse postResponse = adapter.fromJson(
                                    response.errorBody().string());

                            if (postResponse.getMessage().equals(
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
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    presenter.modelOnLoginError();
                }
            });
        }
    }
}
