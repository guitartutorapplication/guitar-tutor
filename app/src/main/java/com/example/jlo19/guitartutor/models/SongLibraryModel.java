package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class SongLibraryModel implements ISongLibraryModel {

    private ISongLibraryPresenter presenter;
    private DatabaseApi api;

    public SongLibraryModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(ISongLibraryPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getSongs() {
        Call<SongsResponse> call = api.getSongs();

        // asynchronously executing call
        call.enqueue(new Callback<SongsResponse>() {
            @Override
            public void onResponse(Call<SongsResponse> call, Response<SongsResponse> response) {
                presenter.modelOnSongsRetrieved(response.body().getSongs());
            }

            @Override
            public void onFailure(Call<SongsResponse> call, Throwable t) {
                presenter.modelOnError();
            }
        });
    }
}
