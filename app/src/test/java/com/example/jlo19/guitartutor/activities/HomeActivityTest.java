package com.example.jlo19.guitartutor.activities;

import android.content.Intent;
import android.os.Build;
import android.widget.Button;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.robolectric.Shadows.shadowOf;

/**
 *  Testing HomeActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

    private HomeActivity activity;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(HomeActivity.class)
                .create().get();
    }

    @Test
    public void whenLearnButtonClicked_LearnViewAllChordsActivityIsStarted() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnLearn);
        button.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LearnAllChordsActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void whenPractiseButtonClicked_PractiseSetupActivityIsStarted() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnPractise);
        button.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(PractiseSetupActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void whenPlayButtonClicked_SongLibraryActivityIsStarted() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnPlay);
        button.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(SongLibraryActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void whenAccountButtonClicked_AccountActivityIsStarted() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnAccount);
        button.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(AccountActivity.class.getName(), intent.getComponent().getClassName());
    }
}
