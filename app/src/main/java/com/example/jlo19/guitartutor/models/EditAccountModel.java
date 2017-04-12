package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ResponseError;
import com.example.jlo19.guitartutor.enums.ValidationResult;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountModel;
import com.example.jlo19.guitartutor.models.retrofit.responses.ResponseWithMessage;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

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
        ValidationResult validationResult = DataValidationModel.validate(
                name, email, confirmEmail, password, confirmPassword);

        if (validationResult != ValidationResult.VALID_DATA) {
            presenter.modelOnValidationFailed(validationResult);
        }
        else {
            // retrieving logged in user's id from shared preferences
            int userId = sharedPreferences.getInt("user_id", 0);

            Call<ResponseWithMessage> call = api.editAccountDetails(userId, name, email, password);

            // asynchronously executing call
            call.enqueue(new Callback<ResponseWithMessage>() {
                @Override
                public void onResponse(Call<ResponseWithMessage> call, Response<ResponseWithMessage> response) {
                    if (response.isSuccessful()) {
                        presenter.modelOnSaveSuccess();
                    }
                    else {
                        // convert raw response when error
                        Gson gson = new Gson();
                        TypeAdapter<ResponseWithMessage> adapter = gson.getAdapter(ResponseWithMessage.class);

                        try {
                            ResponseWithMessage responseWithMessage = adapter.fromJson(
                                    response.errorBody().string());

                            if (responseWithMessage.getMessage().equals(
                                    ResponseError.INVALID_EMAIL.toString())) {
                                presenter.modelOnValidationFailed(ValidationResult.INVALID_EMAIL);
                            }
                            else {
                                presenter.modelOnSaveError();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseWithMessage> call, Throwable t) {
                    presenter.modelOnSaveError();
                }
            });
        }
    }
}
