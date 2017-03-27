package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.RegisterError;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.models.retrofit.RegisterResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class RegisterModel implements IRegisterModel {

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    private IRegisterPresenter presenter;
    private DatabaseApi api;

    public RegisterModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {this.api = api;}

    @Override
    public void setPresenter(IRegisterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void register(String name, String email, String confirmEmail, String password,
                         String confirmPassword) {
        if (name.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty()) {
            presenter.modelOnFieldEmpty();
        }
        else if (!email.equals(confirmEmail)) {
            presenter.modelOnEmailMismatch();
        }
        else if(!password.equals(confirmPassword)) {
            presenter.modelOnPasswordMismatch();
        }
        else if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            presenter.modelOnInvalidEmail();
        }
        else if(password.length() < 8) {
            presenter.modelOnPasswordTooShort();
        }
        // checks if password contains upper case letter
        else if(password.equals(password.toLowerCase())) {
            presenter.modelOnPasswordNoUpperCaseLetter();
        }
        // checks if password contains lower case letter
        else if(password.equals(password.toUpperCase())) {
            presenter.modelOnPasswordNoLowerCaseLetter();
        }
        // checks if password contains number
        else if(!password.matches(".*\\d+.*")) {
            presenter.modelOnPasswordNoNumber();
        }
        else {
            Call<RegisterResponse> call = api.registerUser(name, email, password);

            // asynchronously executing call
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        presenter.modelOnRegisterSuccess();
                    }
                    else {
                        // convert raw response when error
                        Gson gson = new Gson();
                        TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);

                        try {
                            RegisterResponse registerResponse = adapter.fromJson(
                                    response.errorBody().string());

                            if (registerResponse.getMessage().equals(
                                    RegisterError.INVALID_EMAIL.toString())) {
                                presenter.modelOnInvalidEmail();
                            }
                            else if (registerResponse.getMessage().equals(
                                    RegisterError.ALREADY_REGISTERED.toString())) {
                                presenter.modelOnAlreadyRegistered();
                            }
                            else {
                                presenter.modelOnRegisterError();
                            }

                        } catch (IOException e) {
                            presenter.modelOnRegisterError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    presenter.modelOnRegisterError();
                }
            });
        }
    }
}
