package com.example.jlo19.guitartutor.activities;

import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.views.PractiseView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Activity that runs the practise activity
 */
public class PractiseActivity extends BaseWithToolbarActivity implements PractiseView{

    private List<Chord> selectedChords;
    private IPractisePresenter presenter;
    private Button btnStop;
    private SoundPool soundPool;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;
    private TextView txtCountdown;
    private TextView txtFirstChordInstruction;
    private TextView txtFirstChord;
    private List<Integer> soundIds;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    SoundPool.OnLoadCompleteListener onLoadCompleteListener;

    @Override
    public int getLayout() {
        return R.layout.activity_practise;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.practising_chords_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // retrieving selected chords
        selectedChords = getIntent().getExtras().getParcelableArrayList("CHORDS");
        // retrieving chord change
        chordChange = (ChordChange) getIntent().getSerializableExtra("CHORD_CHANGE");
        // retrieving beat speed
        beatSpeed = (BeatSpeed) getIntent().getSerializableExtra("BEAT_SPEED");

        setSoundPool(new SoundPool.Builder().setMaxStreams(2).build());

        txtCountdown = (TextView) findViewById(R.id.txtCountdown);
        txtFirstChordInstruction = (TextView) findViewById(R.id.txtFirstChordInstruction);
        txtFirstChord = (TextView) findViewById(R.id.txtFirstChord);

        // allows injection of presenter
        App.getComponent().inject(this);

        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.viewOnStopPractising();
                }
            });
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
        onLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                presenter.viewOnSoundLoaded(status);
            }
        };
        soundPool.setOnLoadCompleteListener(onLoadCompleteListener);
    }

    @Inject
    public void setPresenter(IPractisePresenter presenter) {
        this.presenter = presenter;
        presenter.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        presenter.setView(this);
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
    public List<Chord> getSelectedChords() {
        return selectedChords;
    }

    @Override
    public void setChordText(final String chord) {
        final TextView txtChord = (TextView) findViewById(R.id.txtChord);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtChord.setText(chord);
            }
        });
    }

    @Override
    public void showStopButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnStop.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void playSound(int index) {
        if (soundPool != null) {
            soundPool.play(soundIds.get(index), 1.0f, 1.0f, 1, 0, 1f);
        }
    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(),
                R.string.practise_error_occurred_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void returnToPractiseSetup() {
        finish();
    }

    @Override
    public void loadSounds(List<String> filenames) {
        soundIds = new ArrayList<>();
        for (String filename : filenames) {
            int resource = getResources().getIdentifier(
                    filename, "raw", getPackageName());
            soundIds.add(soundPool.load(this, resource, 1));
        }
    }

    @Override
    public ChordChange getChordChange() {
        return chordChange;
    }

    @Override
    public BeatSpeed getBeatSpeed() {
        return beatSpeed;
    }

    @Override
    public void setCountdownText(final String second) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCountdown.setText(second);
            }
        });
    }

    @Override
    public void hideCountdown() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCountdown.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void hideFirstChordInstruction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtFirstChord.setVisibility(View.INVISIBLE);
                txtFirstChordInstruction.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void setFirstChordText(String firstChord) {
        txtFirstChord.setText(firstChord);
    }

    @Override
    public void showPractiseSessionSaveSuccess(int achievements) {
        String text = getString(R.string.save_practise_session_success_message) + "\n" +
                getString(R.string.gained_15_achievements_message, achievements);

        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPractiseSessionSaveError() {
        Toast.makeText(getApplicationContext(), getResources().getString(
                R.string.save_practise_session_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPractiseSessionSaveSuccess(int level, int achievements) {
        String text = getString(R.string.save_practise_session_success_message) + "\n" +
                getString(R.string.gained_15_achievements_message, achievements) + "\n" +
                getString(R.string.new_level_message, level);

        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPractiseSessionSaveSuccess() {
        String text = getString(R.string.save_practise_session_success_message) + "\n" +
                getString(R.string.maximum_achievements_message);

        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }
}
