package com.example.jlo19.guitartutor.activities;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;

public class SongActivity extends BaseWithToolbarActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_song;
    }

    @Override
    public String getToolbarTitle() {
        Song song = getIntent().getParcelableExtra("SONG");
        return song.getTitle() + " - " + song.getArtist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieving selected song
        Song song = getIntent().getParcelableExtra("SONG");

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        textView.setText(song.getContents());

        // stops line of text splitting onto multiple lines
        // (so lyrics and chords are always correctly placed)
        int numLines = (song.getContents().split("\r\n")).length;
        textView.setMaxLines(numLines);
    }
}
