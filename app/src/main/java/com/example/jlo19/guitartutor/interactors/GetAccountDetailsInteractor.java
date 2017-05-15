package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.listeners.GetAccountDetailsListener;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.User;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Deals with getting account details from DB (API interaction)
 */
public class GetAccountDetailsInteractor implements IGetAccountDetailsInteractor {

    private GetAccountDetailsListener listener;
    private final DatabaseApi api;

    public GetAccountDetailsInteractor(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setListener(GetAccountDetailsListener listener) {
        this.listener = listener;
    }

    @Override
    public void getAccountDetails(String apiKey, int userId) {
        Call<User> call = api.getAccountDetails(apiKey, userId);

        // asynchronously executing call
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    listener.onAccountDetailsRetrieved(response.body());
                }
                else {
                    listener.onGetAccountDetailsError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onGetAccountDetailsError();
            }
        });
    }
}
