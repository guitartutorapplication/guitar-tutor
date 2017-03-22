package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.jlo19.guitartutor.R;

/**
 * Activity that shows video of selected chord
 */
public class LearnChordVideoActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    MediaPlayer.OnInfoListener onInfoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        showProgressBar();

        // setting video
        String url = getIntent().getExtras().getString("URL");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);

        onInfoListener = new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                    hideProgressBar();
                }
                return false;
            }
        };
        videoView.setOnInfoListener(onInfoListener);

        MediaController mediaCtrl = new MediaController(this);
        mediaCtrl.setMediaPlayer(videoView);
        videoView.setMediaController(mediaCtrl);

        Uri clip = Uri.parse(url);
        videoView.setVideoURI(clip);
        videoView.requestFocus();
        videoView.start();
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(LearnChordVideoActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_video_message));
        progressDialog.show();
    }

    public void hideProgressBar(){
        progressDialog.dismiss();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
