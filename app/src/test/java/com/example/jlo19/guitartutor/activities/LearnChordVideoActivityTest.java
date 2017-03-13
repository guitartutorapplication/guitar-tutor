package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.widget.VideoView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowProgressDialog;

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing LearnChordVideoActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class LearnChordVideoActivityTest {

    private LearnChordVideoActivity activity;
    private String videoUrl;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // giving activity a URL
        videoUrl = "video url";
        Intent intent = new Intent();
        intent.putExtra("URL", videoUrl);

        activity = Robolectric.buildActivity(LearnChordVideoActivity.class, intent)
                .create().get();
    }

    @Test
    public void showProgressBar() {
        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_video_message),
                (shadowOf(dialog).getMessage()));
    }

    @Test
    public void videoViewPrepared_HidesProgressBar() {
        // act
        VideoView videoView = (VideoView) activity.findViewById(R.id.videoView);
        MediaPlayer.OnPreparedListener listener = shadowOf(videoView).getOnPreparedListener();
        listener.onPrepared(new MediaPlayer());

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertFalse(dialog.isShowing());
    }

    @Test
    public void videoURIIsSetToURLFromIntent() {
        // assert
        VideoView videoView = (VideoView) activity.findViewById(R.id.videoView);
        String actualUrl = shadowOf(videoView).getVideoURIString();
        Assert.assertEquals(videoUrl, actualUrl);
    }

    @Test
    public void videoViewHasFocus() {
        // assert
        VideoView videoView = (VideoView) activity.findViewById(R.id.videoView);
        Assert.assertTrue(videoView.hasFocus());
    }

    @Test
    public void videoIsPlaying() {
        // assert
        VideoView videoView = (VideoView) activity.findViewById(R.id.videoView);
        Assert.assertTrue(videoView.isPlaying());
    }
}
