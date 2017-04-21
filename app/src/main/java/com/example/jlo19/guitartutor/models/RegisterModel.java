package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.io.IOException;
import java.util.List;

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
        final List<ValidationError> validationErrors = DataValidationModel.validate(
                name, email, confirmEmail, password, confirmPassword);

        if (!validationErrors.isEmpty()) {
            presenter.modelOnValidationFailed(validationErrors);
        }
        else {
            Call<List<String>> call = api.registerUser(name, email, password);

            // asynchronously executing call
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful()) {
                        presenter.modelOnRegisterSuccess();
                    }
                    else {
                        try {
                            List<ValidationError> validationErrors = DataValidationModel.validateResponse(
                                    response.errorBody().string());

                            if (validationErrors.isEmpty()) {
                                // if API finds no data validation error, show general error
                                presenter.modelOnRegisterError();
                            }
                            else {
                                presenter.modelOnValidationFailed(validationErrors);
                            }

                        } catch (IOException e) {
                            presenter.modelOnRegisterError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    presenter.modelOnRegisterError();
                }
            });
        }
    }
}
