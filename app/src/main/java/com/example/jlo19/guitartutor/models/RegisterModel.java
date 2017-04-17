package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ValidationResult;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.models.retrofit.responses.ResponseWithMessage;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
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
public class RegisterModel implements IRegisterModel {

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
        final ValidationResult validationResult = DataValidationModel.validate(
                name, email, confirmEmail, password, confirmPassword);

        if (validationResult != ValidationResult.VALID_DATA) {
            presenter.modelOnValidationFailed(validationResult);
        }
        else {
            Call<ResponseWithMessage> call = api.registerUser(name, email, password);

            // asynchronously executing call
            call.enqueue(new Callback<ResponseWithMessage>() {
                @Override
                public void onResponse(Call<ResponseWithMessage> call, Response<ResponseWithMessage> response) {
                    if (response.isSuccessful()) {
                        presenter.modelOnRegisterSuccess();
                    }
                    else {
                        // convert raw response when error
                        Gson gson = new Gson();
                        TypeAdapter<ResponseWithMessage> adapter = gson.getAdapter(ResponseWithMessage.class);

                        try {
                            ResponseWithMessage responseWithMessage = adapter.fromJson(
                                    response.errorBody().string());

                            ValidationResult validationResult = DataValidationModel.validateResponse(
                                    responseWithMessage.getMessage());

                            if (validationResult == ValidationResult.VALID_DATA) {
                                // if API finds no data validation error, show general error
                                presenter.modelOnRegisterError();
                            }
                            else {
                                presenter.modelOnValidationFailed(validationResult);
                            }

                        } catch (IOException e) {
                            presenter.modelOnRegisterError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseWithMessage> call, Throwable t) {
                    presenter.modelOnRegisterError();
                }
            });
        }
    }
}
