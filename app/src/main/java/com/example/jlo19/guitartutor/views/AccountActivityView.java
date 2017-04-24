package com.example.jlo19.guitartutor.views;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

/**
 * View interface for AccountActivityActivity
 */
public interface AccountActivityView extends ProgressBarView {
    void setAccountActivity(List<Chord> chord);
    void showError();
    void finishActivity();
}
