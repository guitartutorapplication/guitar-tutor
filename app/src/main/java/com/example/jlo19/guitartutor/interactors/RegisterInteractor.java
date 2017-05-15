package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.enums.ValidationError;
import com.example.jlo19.guitartutor.listeners.RegisterListener;
import com.example.jlo19.guitartutor.interactors.interfaces.IRegisterInteractor;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.example.jlo19.guitartutor.validation.DataValidator;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Deals with registering for account on DB (API interaction)
 */
public class RegisterInteractor implements IRegisterInteractor {

    private RegisterListener listener;
    private final DatabaseApi api;

    public RegisterInteractor(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setListener(RegisterListener listener) {
        this.listener = listener;
    }

    @Override
    public void register(String name, String email, String password) {
        Call<List<String>> call = api.registerUser(name, email, password);

        // asynchronously executing call
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    listener.onRegisterSuccess();
                } else {
                    try {
                        // see if API returned any validation errors
                        List<ValidationError> validationErrors = DataValidator.validateResponse(
                                response.errorBody().string());

                        if (validationErrors.isEmpty()) {
                            // if API finds no data validation error, show general error
                            listener.onRegisterError();
                        } else {
                            listener.onValidationFailed(validationErrors);
                        }

                    } catch (IOException e) {
                        listener.onRegisterError();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                listener.onRegisterError();
            }
        });
    }
}
