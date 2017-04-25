package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.views.SongView;

import javax.inject.Inject;

public class SongActivity extends BaseWithToolbarActivity implements SongView {

    private ISongPresenter presenter;
    private Song song;
    private ProgressDialog progressDialog;
    private Button btnPlay;
    private Button btnStop;
    private MediaPlayer mediaPlayer;
    
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    MediaPlayer.OnCompletionListener onCompletionListener;

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
        song = getIntent().getParcelableExtra("SONG");

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        textView.setText(song.getContents());

        // stops line of text splitting onto multiple lines
        // (so lyrics and chords are always correctly placed)
        int numLines = (song.getContents().split("\r\n")).length;
        textView.setMaxLines(numLines);

        App.getComponent().inject(this);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnPlay();
            }
        });
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnStop();
            }
        });
    }

    @Inject
    public void setPresenter(ISongPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public String getAudioFilename() {
        return song.getAudioFilename();
    }

    @Override
    public void playAudio(String url) {
        Uri uri = Uri.parse(url);
        mediaPlayer = createMediaPlayer(uri);
        if (mediaPlayer == null) {
            presenter.viewOnAudioLoadFailed();
        }
        else {
            presenter.viewOnAudioLoaded();
            onCompletionListener = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    presenter.viewOnStop();
                }
            };
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.start();
        }
    }

    MediaPlayer createMediaPlayer(Uri uri) {
        return MediaPlayer.create(this, uri);
    }

    @Override
    public void showError() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.loading_demo_message_failure)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void setStopButtonVisibility(int visibility) {
        btnStop.setVisibility(visibility);
    }

    @Override
    public void setPlayButtonVisibility(int visibility) {
        btnPlay.setVisibility(visibility);
    }

    @Override
    public void onDestroy() {
        presenter.viewOnStop();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        presenter.viewOnStop();
        super.onPause();
    }

    @Override
    public void onStop() {
        presenter.viewOnStop();
        super.onStop();
    }

    @Override
    public void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(SongActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_demo_message));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }
}
