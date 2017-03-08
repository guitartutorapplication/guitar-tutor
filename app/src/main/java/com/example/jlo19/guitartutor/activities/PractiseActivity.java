package com.example.jlo19.guitartutor.activities;

import android.content.res.Configuration;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;

public class PractiseActivity extends AppCompatActivity {

    private int streamId;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);

        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(R.string.practising_chords_name);

        // loads metronome sound
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        final int soundId = soundPool.load(PractiseActivity.this, R.raw.metronome_sound, 1);

        final Button btnStop = (Button) findViewById(R.id.btnStop);
        final Button btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamId = soundPool.play(soundId, 1.0f, 1.0f, 1, -1, 0.75f);

                btnStop.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.INVISIBLE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.stop(streamId);

                btnStop.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        soundPool.stop(streamId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
