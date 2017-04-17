package com.example.jlo19.guitartutor.presenters.interfaces;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

/**
 * Interface for AccountActivityPresenter
 */
public interface IAccountActivityPresenter extends IPresenter {
    void setSharedPreferences(SharedPreferences preferences);
    void modelOnError();
    void modelOnAccountActivityRetrieved(List<Chord> chords);
}
