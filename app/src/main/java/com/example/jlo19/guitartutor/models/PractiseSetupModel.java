package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class PractiseSetupModel implements IPractiseSetupModel {

    private DatabaseApi api;
    private IPractiseSetupPresenter presenter;

    public PractiseSetupModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(IPractiseSetupPresenter presenter) {
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
                presenter.modelOnLoadChordsError();
            }
        });
    }

    @Override
    public void chordsSelected(ArrayList<String> selectedChords, int chordChangeIndex) {
        Set<String> uniqueChords = new HashSet<>(selectedChords);

        // if the user has selected less than two chords
        if (selectedChords.size() < 2) {
            presenter.modelOnLessThanTwoChordsSelected();
        }
        // if the user has selected the same chord more than once
        else if (uniqueChords.size() < selectedChords.size()) {
            presenter.modelOnSameSelectedChord();
        }
        else {
            presenter.modelOnCorrectSelectedChords(selectedChords,
                    ChordChange.values()[chordChangeIndex]);
        }
    }
}
