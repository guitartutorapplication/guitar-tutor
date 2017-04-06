package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.ChordsButtonAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.views.LearnAllChordsView;

import javax.inject.Inject;


/**
 * Activity that shows all the chords on the screen
 */
public class LearnAllChordsActivity extends BaseWithToolbarActivity implements LearnAllChordsView {

    private ProgressDialog progressDialog;
    private ILearnAllChordsPresenter presenter;
    private ChordsButtonAdapter chordsButtonAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_all_chords;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.all_chords_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setChordsButtonAdapter(new ChordsButtonAdapter(LearnAllChordsActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnChordRequested(v.getId());
            }
        }));

        // allows injection of presenter
        App.getComponent().inject(this);
    }

    @Inject
    public void setPresenter(ILearnAllChordsPresenter presenter) {
        this.presenter = presenter;
        presenter.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        presenter.setView(this);
    }

    public void showError(){
        Toast.makeText(getApplicationContext(),
                R.string.loading_chords_message_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLearnChordActivity(Chord selectedChord, boolean learntChord) {
        // passing selected chord to new activity
        Intent intent = new Intent(getBaseContext(), LearnChordActivity.class);
        intent.putExtra("CHORD", selectedChord);
        intent.putExtra("LEARNT_CHORD", learntChord);
        startActivity(intent);
    }

    @Override
    public void addChordButton(int buttonId) {
        chordsButtonAdapter.addButton(buttonId);
    }

    @Override
    public void setChordButtonText(int buttonId, String text) {
        chordsButtonAdapter.setButtonText(buttonId, text);
    }

    @Override
    public void enableChordButton(int buttonId, boolean isEnabled) {
        chordsButtonAdapter.enableButton(buttonId, isEnabled);
    }

    @Override
    public void setChordButtonBackground(int buttonId, String doneIdentifier, String levelNumberIdentifier) {
        chordsButtonAdapter.setButtonBackground(buttonId, doneIdentifier, levelNumberIdentifier);
    }

    @Override
    public void setChordButtons() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(chordsButtonAdapter);
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(LearnAllChordsActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chords_message));
        progressDialog.show();
    }

    public void hideProgressBar(){
        progressDialog.dismiss();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @VisibleForTesting
    void setChordsButtonAdapter(ChordsButtonAdapter adapter) {
        chordsButtonAdapter = adapter;
    }
}
