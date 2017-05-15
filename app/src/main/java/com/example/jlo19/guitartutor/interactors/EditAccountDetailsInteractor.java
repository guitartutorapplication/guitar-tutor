package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.listeners.EditAccountDetailsListener;
import com.example.jlo19.guitartutor.interactors.interfaces.IEditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.example.jlo19.guitartutor.validation.DataValidator;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Deals with editing account details in DB (API interaction)
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
                        // see if API returned any validation errors
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
