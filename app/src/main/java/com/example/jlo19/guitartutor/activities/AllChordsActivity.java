package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.ChordsButtonAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.GetAllChordsPresenter;
import com.example.jlo19.guitartutor.views.GetAllChordsView;

import java.util.List;

import javax.inject.Inject;


/**
 * Activity that shows all the chords on the screen
 */
public class AllChordsActivity extends AppCompatActivity implements GetAllChordsView {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chords);
        // allows injection of presenter
        App.getComponent().inject(this);
    }

    @Inject
    public void setPresenter(GetAllChordsPresenter presenter) {
        presenter.setView(this);
    }

    public void setChords(final List<Chord> chords) {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        // setting buttons in grid view with on click event to start ChordActivity
        gridView.setAdapter(new ChordsButtonAdapter(AllChordsActivity.this, chords,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int chordId = v.getId();
                        // passing selected chord to new activity
                        Intent intent = new Intent(getBaseContext(), ChordActivity.class);
                        intent.putExtra("CHORD", chords.get(chordId));
                        startActivity(intent);
                    }
                }));
    }

    public void showError(){
        // error pop up message
        Toast.makeText(getApplicationContext(),
                R.string.loading_chords_message_failure, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(AllChordsActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chords_message));
        progressDialog.show();
    }

    public void setToolbarTitleText() {
        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(R.string.all_chords_name);
    }

    public void hideProgressBar(){
        progressDialog.dismiss();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
