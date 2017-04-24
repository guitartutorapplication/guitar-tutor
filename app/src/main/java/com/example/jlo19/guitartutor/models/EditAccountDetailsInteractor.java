package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.listeners.EditAccountDetailsListener;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.example.jlo19.guitartutor.validation.DataValidator;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to DB API
 */
public class EditAccountDetailsInteractor implements IEditAccountDetailsInteractor {

    private final DatabaseApi api;
    private EditAccountDetailsListener listener;

    public EditAccountDetailsInteractor(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setListener(EditAccountDetailsListener listener) {
        this.listener = listener;
    }

    @Override
    public void save(String apiKey, int userId, String name, String email, String password) {
        Call<List<String>> call = api.editAccountDetails(apiKey, userId, name, email, password);

        // asynchronously executing call
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    listener.onSaveSuccess();
                } else {
                    try {
                        List<ValidationError> validationErrors = DataValidator.validateResponse(
                                response.errorBody().string());

                        if (validationErrors.isEmpty()) {
                            // if API finds no data validation error, show general error
                            listener.onSaveError();
                        } else {
                            listener.onValidationFailed(validationErrors);
                        }

                    } catch (IOException e) {
                        listener.onSaveError();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                listener.onSaveError();
            }
        });
    }
}
