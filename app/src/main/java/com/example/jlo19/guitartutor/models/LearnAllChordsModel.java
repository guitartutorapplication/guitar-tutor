package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnAllChordsModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.ChordsResponse;
import com.example.jlo19.guitartutor.models.retrofit.User;
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
    private List<Chord> allChords;
    private List<Integer> userChordsId;
    private int userLevel;

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
        // retrieving all chords
        Call<ChordsResponse> chordsCall = api.getChords();

        chordsCall.enqueue(new Callback<ChordsResponse>() {
            @Override
            public void onResponse(Call<ChordsResponse> call, final Response<ChordsResponse>
                    chordsResponse) {
                allChords = chordsResponse.body().getChords();

                // retrieving logged in user's id from shared preferences
                final int userId = sharedPreferences.getInt("user_id", 0);
                // retrieving chords that user has learnt
                Call<UserChordsResponse> userChordsCall = api.getUserChords(userId);

                userChordsCall.enqueue(new Callback<UserChordsResponse>() {
                    @Override
                    public void onResponse(Call<UserChordsResponse> call, Response<UserChordsResponse>
                            userChordsResponse) {
                        setUserChords(userChordsResponse.body().getUserChords());
                        // retrieving user's level (so know which chords are unlocked)
                        Call<User> userCall = api.getAccountDetails(userId);

                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    userLevel = response.body().getLevel();
                                    // only successful if all 3 calls to API are a success
                                    presenter.modelOnChordsAndDetailsRetrieved();
                                }
                                else {
                                    presenter.modelOnError();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                presenter.modelOnError();
                            }
                        });
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

    @Override
    public List<Chord> getAllChords() {
        return allChords;
    }

    @Override
    public int getUserLevel() {
        return userLevel;
    }

    @Override
    public List<Integer> getUserChords() {
        return userChordsId;
    }

    private void setUserChords(List<UserChord> userChords) {
        userChordsId = new ArrayList<>();
        for (int i = 0; i < userChords.size(); i++) {
            userChordsId.add(userChords.get(i).getChordId());
        }
    }
}
