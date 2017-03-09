package com.example.jlo19.guitartutor.activities;

import android.content.res.Configuration;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;

import java.util.List;

public class PractiseActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private boolean requestStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);

        // retrieving selected chords
        final List<String> selectedChords = getIntent().getExtras().getStringArrayList("CHORDS");

        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(R.string.practising_chords_name);

        if (selectedChords != null) {
            // loads metronome sound
            soundPool = new SoundPool.Builder().setMaxStreams(1).build();
            final int soundId = soundPool.load(PractiseActivity.this, R.raw.metronome_sound, 1);

            final Button btnStop = (Button) findViewById(R.id.btnStop);
            final Button btnStart = (Button) findViewById(R.id.btnStart);

            final TextView txtChord = (TextView) findViewById(R.id.txtChord);
            txtChord.setText(selectedChords.get(0));

            // create task to act as timer
            final Runnable timerTask = new Runnable() {
                @Override
                public void run() {
                    while (!requestStop) {
                        try {
                            for (int i = 0; i < selectedChords.size(); i++) {
                                // update to display new chord
                                final String selectedChord = selectedChords.get(i);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtChord.setText(selectedChord);
                                    }
                                });

                                // play sound for 8 seconds
                                for (int j = 0; j < 8; j++) {
                                    if (requestStop) {
                                        return;
                                    }
                                    soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 0.75f);
                                    Thread.sleep(1000);
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestStop = false;
                    new Thread(timerTask).start();

                    btnStop.setVisibility(View.VISIBLE);
                    btnStart.setVisibility(View.INVISIBLE);
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestStop = true;
                    txtChord.setText(selectedChords.get(0));
                    btnStop.setVisibility(View.INVISIBLE);
                    btnStart.setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),
                    R.string.loading_chords_message_failure, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        requestStop = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
