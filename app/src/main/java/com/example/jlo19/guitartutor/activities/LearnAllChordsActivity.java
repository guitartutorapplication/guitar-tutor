package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.ChordsButtonAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPresenter;
import com.example.jlo19.guitartutor.views.LearnAllChordsView;

import java.util.List;

import javax.inject.Inject;


/**
 * Activity that shows all the chords on the screen
 */
public class LearnAllChordsActivity extends BaseWithToolbarActivity implements LearnAllChordsView {

    private ProgressDialog progressDialog;

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

        // allows injection of presenter
        App.getComponent().inject(this);
    }

    @Inject
    public void setPresenter(IPresenter presenter) {
        presenter.setView(this);
    }

    public void setChords(final List<Chord> chords) {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        // setting buttons in grid view with on click event to start LearnChordActivity
        gridView.setAdapter(new ChordsButtonAdapter(LearnAllChordsActivity.this, chords,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int chordId = v.getId();
                        // passing selected chord to new activity
                        Intent intent = new Intent(getBaseContext(), LearnChordActivity.class);
                        intent.putExtra("CHORD", chords.get(chordId));
                        startActivity(intent);
                    }
                }));
    }

    public void showError(){
        Toast.makeText(getApplicationContext(),
                R.string.loading_chords_message_failure, Toast.LENGTH_SHORT).show();
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
}
