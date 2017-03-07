package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.ChordPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing ChordActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class ChordActivityTest {

    private ChordActivity activity;
    private ChordPresenter presenter;
    private Chord selectedChord;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        // giving activity a selected chord
        selectedChord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4");
        Intent intent = new Intent();
        intent.putExtra("CHORD", selectedChord);

        activity = Robolectric.buildActivity(ChordActivity.class, intent)
                .create().get();
        presenter = PowerMockito.mock(ChordPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_chord_message),
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
        junit.framework.Assert.assertFalse(dialog.isShowing());
    }

    @Test
    public void getChord_ReturnsChordFromIntent() {
        // act
        Chord actual = activity.getChord();

        // assert
        Assert.assertEquals(selectedChord, actual);
    }

    @Test
    public void getContext_ReturnsApplicationContext() {
        // act
        Context actual = activity.getContext();

        // assert
        Assert.assertEquals(getApp(), actual);
    }

    @Test
    public void showError_MakesToastWithErrorMessage() {
        // act
        activity.showError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources()
                .getString(R.string.loading_chord_image_message_failure),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void setImage_SetsImageBitmapInImageView() {
        // act
        Bitmap expected = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        activity.setImage(expected);

        // assert
        ImageView view = (ImageView) activity.findViewById(R.id.imageView);
        Drawable actual = view.getDrawable();
        Assert.assertEquals(new BitmapDrawable(getApp().getResources(),expected), actual);
    }

    @Test
    public void watchButtonClicked_CallsGetVideoOnPresenter() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnWatch);
        button.performClick();

        // assert
        Mockito.verify(presenter).getVideo();
    }

}
