package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.UserChord;
import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to database API
 */
public class LearnAllChordsModel implements ILearnAllChordsModel {

    private ILearnAllChordsPresenter presenter;
    private DatabaseApi api;
    private SharedPreferences sharedPreferences;

    public LearnAllChordsModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void setApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void setPresenter(ILearnAllChordsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getChords() {
        Call<ChordsResponse> chordsCall = api.getChords();

        // asynchronously executing call
        chordsCall.enqueue(new Callback<ChordsResponse>() {
            @Override
            public void onResponse(Call<ChordsResponse> call, final Response<ChordsResponse>
                    chordsResponse) {
                // retrieving logged in user's id from shared preferences
                final int userId = sharedPreferences.getInt("user_id", 0);
                Call<UserChordsResponse> userChordsCall = api.getUserChords(userId);

                // asynchronously executing call
                userChordsCall.enqueue(new Callback<UserChordsResponse>() {
                    @Override
                    public void onResponse(Call<UserChordsResponse> call, Response<UserChordsResponse>
                            userChordsResponse) {
                        // sending both all the chords and chords that user has already learnt
                        List<UserChord> userChords = userChordsResponse.body().getUserChords();
                        List<Integer> userChordsId = new ArrayList<>();
                        for (int i = 0; i < userChords.size(); i++) {
                            userChordsId.add(userChords.get(i).getChordId());
                        }

                        presenter.modelOnChordsRetrieved(chordsResponse.body().getChords(),
                                userChordsId);
                    }

                    @Override
                    public void onFailure(Call<UserChordsResponse> call, Throwable t) {
                        presenter.modelOnError();
                    }
                });
            }

            @Override
            public void onFailure(Call<ChordsResponse> call, Throwable t) {
                presenter.modelOnError();
            }
        });
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
