package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.listeners.LoginListener;
import com.example.jlo19.guitartutor.interactors.interfaces.ILoginInteractor;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Deals with log in to account on DB (API interaction)
 */
public class LoginInteractor implements ILoginInteractor {

    private LoginListener listener;
    private final DatabaseApi api;

    public LoginInteractor(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setListener(LoginListener listener) {
        this.listener = listener;
    }

    @Override
    public void login(String email, String password) {
        Call<User> call = api.loginUser(email, password);

        // asynchronously executing call
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    listener.onLoginSuccess(response.body());
                }
                else {
                    listener.onLoginError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onLoginError();
            }
        });
    }
}
