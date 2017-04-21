package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountModel;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to DB API
 */
public class EditAccountModel extends DataValidationModel implements IEditAccountModel {

    private DatabaseApi api;
    private IEditAccountPresenter presenter;
    private SharedPreferences sharedPreferences;

    public EditAccountModel() {
        App.getComponent().inject(this);}

    @Inject
    public void setApi(DatabaseApi api) {this.api = api;}

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void setPresenter(IEditAccountPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void save(String name, String email, String confirmEmail, String password, String confirmPassword) {
        List<ValidationError> errors = DataValidationModel.validate(
                name, email, confirmEmail, password, confirmPassword);

        if (!errors.isEmpty()) {
            presenter.modelOnValidationFailed(errors);
        }
        else {
            // retrieving logged in user's id & api key from shared preferences
            int userId = sharedPreferences.getInt("user_id", 0);
            String apiKey = sharedPreferences.getString("api_key", "");

            Call<List<String>> call = api.editAccountDetails(apiKey, userId, name, email, password);

            // asynchronously executing call
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful()) {
                        presenter.modelOnSaveSuccess();
                    }
                    else {
                        try {
                            List<ValidationError> validationErrors = DataValidationModel.validateResponse(
                                    response.errorBody().string());

                            if (validationErrors.isEmpty()) {
                                // if API finds no data validation error, show general error
                                presenter.modelOnSaveError();
                            }
                            else {
                                presenter.modelOnValidationFailed(validationErrors);
                            }

                        } catch (IOException e) {
                            presenter.modelOnSaveError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    presenter.modelOnSaveError();
                }
            });
        }
    }
}
