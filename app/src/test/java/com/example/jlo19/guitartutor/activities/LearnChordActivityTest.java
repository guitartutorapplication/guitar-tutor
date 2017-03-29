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
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;

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
 * Testing LearnChordActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class LearnChordActivityTest {

    private LearnChordActivity activity;
    private ILearnChordPresenter presenter;
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

        activity = Robolectric.buildActivity(LearnChordActivity.class, intent)
                .create().get();
        presenter = PowerMockito.mock(ILearnChordPresenter.class);
        activity.setPresenter(presenter);
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
        Assert.assertFalse(dialog.isShowing());
    }

    @Test
    public void getChord_ReturnsChordFromIntent() {
        // act
        Chord actualSelectedChord = activity.getChord();

        // assert
        Assert.assertEquals(selectedChord, actualSelectedChord);
    }

    @Test
    public void getContext_ReturnsApplicationContext() {
        // act
        Context actualContext = activity.getContext();

        // assert
        Assert.assertEquals(getApp(), actualContext);
    }

    @Test
    public void showImageLoadError_MakesToastWithErrorMessage() {
        // act
        activity.showImageLoadError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources()
                        .getString(R.string.loading_chord_image_message_failure),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showVideoLoadError_MakesToastWithErrorMessage() {
        // act
        activity.showVideoLoadError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources()
                        .getString(R.string.loading_chord_video_message_failure),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void setImage_SetsImageBitmapInImageView() {
        // act
        Bitmap expectedImage = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        activity.setImage(expectedImage);

        // assert
        ImageView view = (ImageView) activity.findViewById(R.id.imageView);
        Drawable actualDrawable = view.getDrawable();
        Assert.assertEquals(new BitmapDrawable(getApp().getResources(),expectedImage), actualDrawable);
    }

    @Test
    public void watchButtonClicked_CallsVideoRequestedOnPresenter() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnWatch);
        button.performClick();

        // assert
        Mockito.verify(presenter).viewOnVideoRequested();
    }

    @Test
    public void helpButtonClicked_LearnDiagramHelpActivityIsStarted() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnHelp);
        button.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LearnDiagramHelpActivity.class.getName(),
                intent.getComponent().getClassName());
    }

    @Test
    public void playVideoCalled_LearnChordVideoActivityIsStartedWithURL() {
        // act
        String expectedUrl = "video url";
        activity.playVideo(expectedUrl);

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LearnChordVideoActivity.class.getName(),
                intent.getComponent().getClassName());
        // checks URL is passed through
        Assert.assertEquals(expectedUrl, intent.getExtras().getString("URL"));
    }

}
