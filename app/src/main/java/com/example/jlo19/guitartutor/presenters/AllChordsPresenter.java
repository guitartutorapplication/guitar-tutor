package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.models.ChordsResponse;
import com.example.jlo19.guitartutor.services.DatabaseApi;
import com.example.jlo19.guitartutor.services.DatabaseService;
import com.example.jlo19.guitartutor.views.AllChordsView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Presenter which provides the AllChordActivity with data from the database API
 */
public class AllChordsPresenter {

    private AllChordsView view;
    private DatabaseApi api;

    public AllChordsPresenter() {
        api = DatabaseService.getApi();
    }

    public void setView(AllChordsView view) {
        this.view = view;
        view.setToolbarTitleText();
        view.showProgressBar();

        getChords();
    }

    private void getChords() {
        Call<ChordsResponse> call = api.getChords();

        // asynchronously executing call
        call.enqueue(new Callback<ChordsResponse>() {
            @Override
            public void onResponse(Call<ChordsResponse> call, Response<ChordsResponse> response) {
                view.hideProgressBar();

                List<Chord> chords = response.body().getChords();
                view.setChords(chords);
            }

            @Override
            public void onFailure(Call<ChordsResponse> call, Throwable t) {
                view.hideProgressBar();
                view.showError();
            }
        });
    }
}
