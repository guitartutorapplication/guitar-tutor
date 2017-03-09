package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.GetAllChordsPresenter;
import com.example.jlo19.guitartutor.views.GetAllChordsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

public class PractiseSetupActivity extends AppCompatActivity implements GetAllChordsView {

    private ProgressDialog progressDialog;
    private String defaultSpinnerOption;
    private List<Spinner> spnChords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_setup);

        Spinner spnChord1 = (Spinner) findViewById(R.id.spnChord1);
        Spinner spnChord2 = (Spinner) findViewById(R.id.spnChord2);
        Spinner spnChord3 = (Spinner) findViewById(R.id.spnChord3);
        Spinner spnChord4 = (Spinner) findViewById(R.id.spnChord4);
        spnChords = Arrays.asList(spnChord1, spnChord2, spnChord3, spnChord4);

        defaultSpinnerOption = getResources().getString(R.string.select_chord_instruction);

        // allows injection of presenter
        App.getComponent().inject(this);

        Button btnPractise = (Button) findViewById(R.id.btnPractise);
        btnPractise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedChords = new ArrayList<>();
                for(int i = 0; i < spnChords.size(); i++) {
                    String selectedChord = spnChords.get(i).getSelectedItem().toString();
                    if (!Objects.equals(selectedChord, defaultSpinnerOption)) {
                        selectedChords.add(selectedChord);
                    }
                }

                Set<String> uniqueChords = new HashSet<>(selectedChords);

                // if the user has selected less than two chords
                if (selectedChords.size() < 2) {
                    Toast.makeText(getApplicationContext(),
                            R.string.less_than_two_selected_chords_error, Toast.LENGTH_SHORT).show();
                }
                // if the user has selected the same chord twice
                else if (uniqueChords.size() < selectedChords.size()) {
                    Toast.makeText(getApplicationContext(),
                            R.string.same_chord_selected_error, Toast.LENGTH_SHORT).show();
                }
                else {
                    // passing through selected chords to new activity
                    Intent intent = new Intent(getBaseContext(), PractiseActivity.class);
                    intent.putExtra("CHORDS", selectedChords);
                    startActivity(intent);
                }
            }
        });
    }

    @Inject
    public void setPresenter(GetAllChordsPresenter presenter) {
        presenter.setView(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void setToolbarTitleText() {
        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(R.string.practise_setup_name);
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(PractiseSetupActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chords_message));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void setChords(List<Chord> chords) {
        // setting chords in spinners
        String[] spnItems = new String[chords.size()+1];
        spnItems[0] = defaultSpinnerOption;

        for(int i = 0; i < chords.size(); i++) {
            spnItems[i+1] = chords.get(i).toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spnItems);

        for(int i = 0; i < spnChords.size(); i++) {
            spnChords.get(i).setAdapter(adapter);
        }
    }

    @Override
    public void showError() {
        // error pop up message
        Toast.makeText(getApplicationContext(),
                R.string.loading_chords_message_failure, Toast.LENGTH_SHORT).show();
    }
}
