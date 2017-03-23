package com.example.jlo19.guitartutor.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.Song;

public class SongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // retrieving selected song
        Song song = getIntent().getParcelableExtra("SONG");

        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(song.getTitle() + " - " + song.getArtist());

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        textView.setText(song.getContents());

        // stops line of text splitting onto multiple lines
        // (so lyrics and chords are always correctly placed)
        int numLines = (song.getContents().split("\r\n")).length;
        textView.setMaxLines(numLines);
    }
}
