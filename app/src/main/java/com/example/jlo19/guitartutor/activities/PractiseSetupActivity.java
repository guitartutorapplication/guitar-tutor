package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.ChordsListAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.views.PractiseSetupView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Activity to decide set up for practise activity
 */
public class PractiseSetupActivity extends BaseWithToolbarActivity implements PractiseSetupView {

    private ProgressDialog progressDialog;
    private List<Spinner> spnChords;
    private IPractiseSetupPresenter presenter;
    private SoundPool soundPool;
    private int soundId;
    private Button btnPreview;

    @Override
    public int getLayout() {
        return R.layout.activity_practise_setup;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.practise_setup_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up soundpool with a maximum of one sound playing at once
        setSoundPool(new SoundPool.Builder().setMaxStreams(1).build());

        // adds all spinners to list for easy access
        Spinner spnChord1 = (Spinner) findViewById(R.id.spnChord1);
        Spinner spnChord2 = (Spinner) findViewById(R.id.spnChord2);
        Spinner spnChord3 = (Spinner) findViewById(R.id.spnChord3);
        Spinner spnChord4 = (Spinner) findViewById(R.id.spnChord4);
        spnChords = Arrays.asList(spnChord1, spnChord2, spnChord3, spnChord4);

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
                // retrieving selected chords from spinners
                List<Chord> selectedChords = new ArrayList<>();
                for(int i = 0; i < spnChords.size(); i++) {
                    Chord selectedChord = (Chord) spnChords.get(i).getSelectedItem();
                    selectedChords.add(selectedChord);
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
        presenter.viewOnDestroy();
        soundPool.release();
        soundPool = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.viewOnPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.viewOnStop();
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
        // set list of chords in adapter for each spinner
        ChordsListAdapter adapter = new ChordsListAdapter(this, chords);

        for(int i = 0; i < spnChords.size(); i++) {
            spnChords.get(i).setAdapter(adapter);
        }
    }

    @Override
    public void showLoadChordsError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.loading_chords_message_failure)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmError();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showLessThanTwoChordsSelectedError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.less_than_two_selected_chords_error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showSameSelectedChordError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.same_chord_selected_error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void startPractiseActivity(List<Chord> selectedChords, ChordChange chordChange,
                                      BeatSpeed beatSpeed) {
        // passing through selected chords, chord change and beat speed choices to new activity
        Intent intent = new Intent(getBaseContext(), PractiseActivity.class);
        intent.putParcelableArrayListExtra("CHORDS", (ArrayList<Chord>) selectedChords);
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
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.practise_beat_preview_error_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
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

    @Override
    public void finishActivity() {
        finish();
    }
}
