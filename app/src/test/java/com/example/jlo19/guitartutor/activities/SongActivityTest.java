package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowProgressDialog;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.robolectric.Shadows.shadowOf;

/**
 * Testing SongActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class SongActivityTest {
    private SongActivity activity;
    private Song selectedSong;
    private ISongPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        // giving activity a selected song
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        selectedSong = new Song("Adventure of a Lifetime", "Coldplay", "Adventure of a Lifetime.wav",
                "Test contents of song \r\n actual contains song lyrics and chords \r\n", chords);
        Intent intent = new Intent();
        intent.putExtra("SONG", selectedSong);

        activity = Robolectric.buildActivity(SongActivity.class, intent)
                .create().get();

        presenter = Mockito.mock(ISongPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void setsToolbarTextWithSongTitleAndArtist() {
        // assert
        String expectedToolbarText = selectedSong.getTitle() + " - " + selectedSong.getArtist();

        TextView textView = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(expectedToolbarText, textView.getText().toString());
    }

    @Test
    public void accountButtonClicked_StartsAccountActivity() {
        // act
        Button btnAccount = (Button) activity.findViewById(R.id.btnAccount);
        btnAccount.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(AccountActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void setsScrollingMovingMethodOnMainTextView() {
        // assert
        TextView textView = (TextView) activity.findViewById(R.id.textView);
        Assert.assertEquals(ScrollingMovementMethod.class.getName(), textView.getMovementMethod()
                .getClass().getName());
    }

    @Test
    public void setsMainTextToContentsFromSong() {
        // assert
        TextView textView = (TextView) activity.findViewById(R.id.textView);
        Assert.assertEquals(selectedSong.getContents(), textView.getText().toString());
    }

    @Test
    public void setMaxLinesOfMainTextToNumberOfLinesInContents() {
        // assert
        TextView textView = (TextView) activity.findViewById(R.id.textView);
        Assert.assertEquals((selectedSong.getContents().split("\r\n")).length,
                textView.getMaxLines());
    }

    @Test
    public void homeButtonClicked_StartsHomeActivityWithFlagsSetAndSongActivityIsFinished() {
        // act
        Button btnHome = (Button) activity.findViewById(R.id.btnHome);
        btnHome.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
        // check flags
        Assert.assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK,
                intent.getFlags());
        Assert.assertTrue(activity.isFinishing());
    }

    @Test
    public void playButtonClicked_CallsPlayOnPresenter() {
        // act
        Button btnPlay = (Button) activity.findViewById(R.id.btnPlay);
        btnPlay.performClick();

        // assert
        Mockito.verify(presenter).viewOnPlay();
    }

    @Test
    public void stopButtonClicked_CallsStopOnPresenter() {
        // act
        Button btnStop = (Button) activity.findViewById(R.id.btnStop);
        btnStop.performClick();

        // assert
        Mockito.verify(presenter).viewOnStop();
    }

    @Test
    public void getAudioFilename_ReturnsAudioFilenameFromSongFromIntent() {
        // act
        String actualFilename = activity.getAudioFilename();

        // assert
        Assert.assertEquals(selectedSong.getAudioFilename(), actualFilename);
    }

    @Test
    public void playAudio_NullMediaPlayer_CallsAudioLoadFailedOnPresenter() {
        // arrange
        SongActivity activitySpy = Mockito.spy(activity);
        Mockito.when(activitySpy.createMediaPlayer(any(Uri.class))).thenReturn(null);

        // act
        activitySpy.playAudio("url");

        // assert
        Mockito.verify(presenter).viewOnAudioLoadFailed();
    }

    @Test
    public void playAudio_CallsAudioLoadedOnPresenter() {
        // arrange
        MediaPlayer mediaPlayer = PowerMockito.mock(MediaPlayer.class);
        SongActivity activitySpy = Mockito.spy(activity);
        Mockito.when(activitySpy.createMediaPlayer(any(Uri.class))).thenReturn(mediaPlayer);

        // act
        activitySpy.playAudio("url");

        // assert
        Mockito.verify(presenter).viewOnAudioLoaded();
    }

    @Test
    public void playAudio_CallsStartOnMediaPlayer() {
        // arrange
        MediaPlayer mediaPlayer = PowerMockito.mock(MediaPlayer.class);
        SongActivity activitySpy = Mockito.spy(activity);
        Mockito.when(activitySpy.createMediaPlayer(any(Uri.class))).thenReturn(mediaPlayer);

        // act
        activitySpy.playAudio("url");

        // assert
        Mockito.verify(mediaPlayer).start();
    }

    @Test
    public void playAudio_MediaPlayerOnCompletion_CallsStopOnPresenter() {
        // arrange
        MediaPlayer mediaPlayer = PowerMockito.mock(MediaPlayer.class);
        SongActivity activitySpy = Mockito.spy(activity);
        Mockito.when(activitySpy.createMediaPlayer(any(Uri.class))).thenReturn(mediaPlayer);
        activitySpy.playAudio("url");

        // act
        activitySpy.onCompletionListener.onCompletion(Mockito.mock(MediaPlayer.class));

        // assert
        Mockito.verify(presenter).viewOnStop();
    }

    @Test
    public void showError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_demo_message_failure),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void getContext_ReturnsContext() {
        // act
        Context actualContext = activity.getContext();

        // assert
        Assert.assertEquals(getApp(), actualContext);
    }

    @Test
    public void setStopButtonVisibility_SetsButtonVisibility() {
        // act
        int visibility = View.VISIBLE;
        activity.setStopButtonVisibility(visibility);

        // assert
        Button btnStop = (Button) activity.findViewById(R.id.btnStop);
        Assert.assertEquals(visibility, btnStop.getVisibility());
    }

    @Test
    public void setPlayButtonVisibility_SetsButtonVisibility() {
        // act
        int visibility = View.INVISIBLE;
        activity.setPlayButtonVisibility(visibility);

        // assert
        Button btnPlay = (Button) activity.findViewById(R.id.btnPlay);
        Assert.assertEquals(visibility, btnPlay.getVisibility());
    }

    @Test
    public void onDestroy_CallsStopOnPresenter() {
        // act
        activity.onDestroy();

        // assert
        Mockito.verify(presenter).viewOnStop();
    }

    @Test
    public void onPause_CallsStopOnPresenter() {
        // act
        activity.onPause();

        // assert
        Mockito.verify(presenter).viewOnStop();
    }

    @Test
    public void onStop_CallsStopOnPresenter() {
        // act
        activity.onStop();

        // assert
        Mockito.verify(presenter).viewOnStop();
    }

    @Test
    public void playAudio_StopAudio_CallsStopResetReleaseOnMediaPlayer() {
        // assert
        MediaPlayer mediaPlayer = PowerMockito.mock(MediaPlayer.class);
        SongActivity activitySpy = Mockito.spy(activity);
        Mockito.when(activitySpy.createMediaPlayer(any(Uri.class))).thenReturn(mediaPlayer);
        activitySpy.playAudio("url");

        // act
        activitySpy.stopAudio();

        // assert
        Mockito.verify(mediaPlayer).stop();
        Mockito.verify(mediaPlayer).reset();
        Mockito.verify(mediaPlayer).release();
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        org.junit.Assert.assertTrue(dialog.isShowing());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.loading_demo_message),
                (shadowOf(dialog).getMessage()));
    }

    @Test
    public void showProgressBar_hideProgressBar_NoProgressDialogShowing() {
        // arrange
        activity.showProgressBar();

        // act
        activity.hideProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        org.junit.Assert.assertFalse(dialog.isShowing());
    }
}
