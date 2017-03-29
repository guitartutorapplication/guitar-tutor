package com.example.jlo19.guitartutor.activities;

import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

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
 * Testing LearnDiagramHelpActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class LearnDiagramHelpActivityTest {

    private LearnDiagramHelpActivity activity;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(LearnDiagramHelpActivity.class)
                .create().get();
    }

    @Test
    public void setToolbarTitleTextToDiagramHelpName() {
        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.diagram_help_name),
                view.getText().toString());
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
}
