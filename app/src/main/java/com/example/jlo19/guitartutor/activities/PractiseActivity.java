package com.example.jlo19.guitartutor.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.views.PractiseView;

import java.util.List;

import javax.inject.Inject;

public class PractiseActivity extends AppCompatActivity implements PractiseView{

    private List<String> selectedChords;
    private IPractisePresenter presenter;
    private Button btnStart;
    private Button btnStop;
    private SoundPool soundPool;
    private int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // retrieving selected chords
        selectedChords = getIntent().getExtras().getStringArrayList("CHORDS");

        setSoundPool(new SoundPool.Builder().setMaxStreams(1).build());

        // allows injection of presenter
        App.getComponent().inject(this);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.viewOnStartPractising();
                }
            });

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
    }

    @Inject
    public void setPresenter(IPractisePresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.viewOnStopPractising();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void setToolbarTitleText() {
        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(R.string.practising_chords_name);
    }

    @Override
    public List<String> getSelectedChords() {
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
    public void setStopButtonVisibility(int isVisible) {
        btnStop.setVisibility(isVisible);
    }

    @Override
    public void setStartButtonVisibility(int isVisible) {
        btnStart.setVisibility(isVisible);
    }

    @Override
    public void playSound() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(),
                R.string.practise_error_occurred_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startPractiseSetupActivity() {
        Intent intent = new Intent(getBaseContext(), PractiseSetupActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadSound() {
        soundId = soundPool.load(this, R.raw.metronome_sound, 1);
    }
}
