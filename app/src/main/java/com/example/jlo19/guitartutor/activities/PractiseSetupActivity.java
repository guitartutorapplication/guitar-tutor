package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.views.PractiseSetupView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class PractiseSetupActivity extends AppCompatActivity implements PractiseSetupView {

    private ProgressDialog progressDialog;
    private String defaultSpinnerOption;
    private List<Spinner> spnChords;
    private IPractiseSetupPresenter presenter;
    private SoundPool soundPool;
    private int soundId;
    private Button btnPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_setup);

        setSoundPool(new SoundPool.Builder().setMaxStreams(1).build());

        Spinner spnChord1 = (Spinner) findViewById(R.id.spnChord1);
        Spinner spnChord2 = (Spinner) findViewById(R.id.spnChord2);
        Spinner spnChord3 = (Spinner) findViewById(R.id.spnChord3);
        Spinner spnChord4 = (Spinner) findViewById(R.id.spnChord4);
        spnChords = Arrays.asList(spnChord1, spnChord2, spnChord3, spnChord4);

        defaultSpinnerOption = getResources().getString(R.string.select_chord_instruction);

        // allows injection of presenter
        App.getComponent().inject(this);

        final RadioGroup rGroupBeatSpeed = (RadioGroup) findViewById(R.id.rGroupBeatSpeed);

        rGroupBeatSpeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                presenter.viewOnBeatSpeedChanged();
            }
        });

        btnPreview = (Button) findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedBeatSpeedIndex = rGroupBeatSpeed.indexOfChild(findViewById(
                        rGroupBeatSpeed.getCheckedRadioButtonId()));

                presenter.viewOnBeatPreview(selectedBeatSpeedIndex);
            }
        });

        Button btnPractise = (Button) findViewById(R.id.btnPractise);
        btnPractise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // retrieving selected chords (non default option)
                ArrayList<String> selectedChords = new ArrayList<>();
                for(int i = 0; i < spnChords.size(); i++) {
                    String selectedChord = spnChords.get(i).getSelectedItem().toString();
                    if (!Objects.equals(selectedChord, defaultSpinnerOption)) {
                        selectedChords.add(selectedChord);
                    }
                }

                RadioGroup rGroupChordChange = (RadioGroup) findViewById(R.id.rGroupChordChange);
                int selectedChordChangeIndex = rGroupChordChange.indexOfChild(findViewById(
                        rGroupChordChange.getCheckedRadioButtonId()));

                int selectedBeatSpeedIndex = rGroupBeatSpeed.indexOfChild(findViewById(
                        rGroupBeatSpeed.getCheckedRadioButtonId()));

                presenter.viewOnPractise(selectedChords, selectedChordChangeIndex, selectedBeatSpeedIndex);
            }
        });
    }

    @Inject
    public void setPresenter(IPractiseSetupPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
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
    public void showLoadChordsError() {
        Toast.makeText(getApplicationContext(),
                R.string.loading_chords_message_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLessThanTwoChordsSelectedError() {
        Toast.makeText(getApplicationContext(),
                R.string.less_than_two_selected_chords_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSameSelectedChordError() {
        Toast.makeText(getApplicationContext(),
                R.string.same_chord_selected_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startPractiseActivity(ArrayList<String> selectedChords, ChordChange chordChange,
                                      BeatSpeed beatSpeed) {
        // passing through selected chords and chord change to new activity
        Intent intent = new Intent(getBaseContext(), PractiseActivity.class);
        intent.putExtra("CHORDS", selectedChords);
        intent.putExtra("CHORD_CHANGE", chordChange);
        intent.putExtra("BEAT_SPEED", beatSpeed);
        startActivity(intent);
    }

    @Override
    public void playSound() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Override
    public void loadSound() {
        soundId = soundPool.load(this, R.raw.metronome_sound, 1);
    }

    @Override
    public void showPreviewBeatError() {
        Toast.makeText(getApplicationContext(),
                R.string.practise_beat_preview_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void enablePreviewButton(final boolean isEnabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnPreview.setEnabled(isEnabled);
            }
        });
    }
}
