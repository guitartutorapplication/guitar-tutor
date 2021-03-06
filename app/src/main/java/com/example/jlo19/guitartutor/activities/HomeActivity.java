package com.example.jlo19.guitartutor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.jlo19.guitartutor.R;

/**
 * Activity that displays home page
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // setting up on click listeners to start correct activity
        Button btnLearn = (Button) findViewById(R.id.btnLearn);
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LearnAllChordsActivity.class);
                startActivity(intent);
            }
        });

        Button btnPractise = (Button) findViewById(R.id.btnPractise);
        btnPractise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PractiseSetupActivity.class);
                startActivity(intent);
            }
        });

        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SongLibraryActivity.class);
                startActivity(intent);
            }
        });

        Button btnAccount = (Button) findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
