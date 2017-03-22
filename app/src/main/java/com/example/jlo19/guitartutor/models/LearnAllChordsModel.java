package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnViewAllChordsPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class LearnAllChordsModel implements ILearnViewAllChordsModel {

    private ILearnViewAllChordsPresenter presenter;
    private DatabaseApi api;

    public LearnAllChordsModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(ILearnViewAllChordsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getChords() {
        Call<ChordsResponse> call = api.getChords();

        // asynchronously executing call
        call.enqueue(new Callback<ChordsResponse>() {
            @Override
            public void onResponse(Call<ChordsResponse> call, Response<ChordsResponse> response) {
                presenter.modelOnChordsRetrieved(response.body().getChords());
            }

            @Override
            public void onFailure(Call<ChordsResponse> call, Throwable t) {
                presenter.modelOnError();
            }
        });
    }
}
