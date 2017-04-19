package com.example.jlo19.guitartutor.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles connection to Amazon S3 API
 */
public class LearnChordModel implements ILearnChordModel {

    private IAmazonS3Service service;
    private ILearnChordPresenter presenter;
    private DatabaseApi api;
    private SharedPreferences sharedPreferences;

    public LearnChordModel() {
        App.getComponent().inject(this);
    }

    @Inject
    void createAmazonS3Service(IAmazonS3Service service) {
        this.service = service;
        service.setImageListener(this);
        service.setUrlListener(this);
    }

    @Inject
    void setDatabaseApi(DatabaseApi api) {
        this.api = api;
    }

    @Override
    public void onImageDownloadFailed() {
        presenter.modelOnImageDownloadFailed();
    }

    @Override
    public void onImageDownloadSuccess(Bitmap bitmap) {
        presenter.modelOnImageDownloadSuccess(bitmap);
    }

    @Override
    public void onUrlDownloadSuccess(String url) {
        presenter.modelOnUrlDownloadSuccess(url);
    }

    @Override
    public void onUrlDownloadFailed() {
        presenter.modelOnUrlDownloadFailed();
    }

    @Override
    public void setContext(Context context) {
        service.setClient(context);
    }

    @Override
    public void getImage(String filename) {
        service.getImage(filename);
    }

    @Override
    public void setPresenter(ILearnChordPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getVideo(String filename) {
        service.getUrl(filename);
    }

    @Override
    public void addLearntChord(int chordId) {
        // retrieving logged in user's id & api key from shared preferences
        final int userId = sharedPreferences.getInt("user_id", 0);
        final String apiKey = sharedPreferences.getString("api_key", "");
        Call<User> call = api.addUserChord(apiKey, userId, chordId);

        // asynchronously executing call
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    int level = response.body().getLevel();
                    int achievements = response.body().getAchievements();
                    presenter.modelOnLearntChordAdded(level, achievements);
                }
                else {
                    presenter.modelOnAddLearntChordError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                presenter.modelOnAddLearntChordError();
            }
        });
    }

    @Override
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
