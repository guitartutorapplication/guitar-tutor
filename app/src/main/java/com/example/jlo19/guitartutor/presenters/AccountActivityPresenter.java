package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.interfaces.IAccountActivityModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.views.AccountActivityView;
import com.example.jlo19.guitartutor.views.IView;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter that provides the AccountActivity with account details from DB
 */
public class AccountActivityPresenter implements IAccountActivityPresenter {

    private AccountActivityView view;
    private SharedPreferences sharedPreferences;

    @Inject
    public void setModel(IAccountActivityModel model) {
        model.setPresenter(this);
        model.setSharedPreferences(sharedPreferences);
        model.getAccountActivity();
    }

    @Override
    public void setView(IView view) {
        this.view = (AccountActivityView) view;
        this.view.showProgressBar();

        App.getComponent().inject(this);
    }

    @Override
    public void setSharedPreferences(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

    @Override
    public void modelOnError() {
        view.hideProgressBar();
        view.showError();
    }

    @Override
    public void modelOnAccountActivityRetrieved(List<Chord> chords) {
        view.hideProgressBar();
        view.setAccountActivity(chords);
    }
}
