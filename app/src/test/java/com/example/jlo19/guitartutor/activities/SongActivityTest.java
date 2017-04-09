package com.example.jlo19.guitartutor.activities;

import android.content.Intent;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing SongActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class SongActivityTest {
    private SongActivity activity;
    private Song selectedSong;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // giving activity a selected song
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1));
        selectedSong = new Song("Adventure of a Lifetime", "Coldplay",
                "Test contents of song \r\n actual contains song lyrics and chords \r\n", chords);
        Intent intent = new Intent();
        intent.putExtra("SONG", selectedSong);

        activity = Robolectric.buildActivity(SongActivity.class, intent)
                .create().get();
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
    public void homeButtonClicked_StartsHomeActivity() {
        // act
        Button btnHome = (Button) activity.findViewById(R.id.btnHome);
        btnHome.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
    }
}
