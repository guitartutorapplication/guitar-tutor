package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowProgressDialog;

import static android.app.Activity.RESULT_OK;
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
    private boolean learntChord;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        // giving activity a selected chord
        selectedChord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1);
        learntChord = true;
        Intent intent = new Intent();
        intent.putExtra("CHORD", selectedChord);
        intent.putExtra("LEARNT_CHORD", learntChord);

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
    public void startLearnAllChordsActivity_StartsLearnAllChordsActivityAndLearnChordActivityIsFinished() {
        // act
        activity.startLearnAllChordsActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LearnAllChordsActivity.class.getName(), intent.getComponent().getClassName());
        junit.framework.Assert.assertEquals(RESULT_OK, shadowOf(activity).getResultCode());
        Assert.assertTrue(activity.isFinishing());
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
    public void getLearntChord_ReturnsLearntChordFromIntent() {
        // act
        boolean actualLearntChord = activity.getLearntChord();

        // assert
        Assert.assertEquals(learntChord, actualLearntChord);
    }

    @Test
    public void showImageLoadError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showImageLoadError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        junit.framework.Assert.assertEquals(getApp().getResources()
                        .getString(R.string.loading_chord_image_message_failure),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void showImageLoadError_ClicksOkButton_CallsConfirmErrorOnPresenter() {
        // arrange
        activity.showImageLoadError();

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmError();
    }

    @Test
    public void showAddLearntChordSuccess_ShowsAlertDialogWithSuccessMessage() {
        // act
        activity.showAddLearntChordSuccess();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        String expectedText = getApp().getResources()
                .getString(R.string.add_learnt_chord_success_message) + "\n" + getApp().getResources()
                .getString(R.string.maximum_achievements_message);
        Assert.assertEquals(expectedText, shadowOf(dialog).getMessage());
    }

    @Test
    public void showAddLearntChordSuccess_ClicksOkButton_CallsConfirmLearntSuccessOnPresenter() {
        // arrange
        activity.showAddLearntChordSuccess();

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmLearntSuccess();
    }

    @Test
    public void showAddLearntChordSuccessWithAchievements_ShowsAlertDialogWithSuccessMessage() {
        // act
        int achievements = 2100;
        activity.showAddLearntChordSuccess(achievements);

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        String expectedText = getApp().getResources().getString(R.string.add_learnt_chord_success_message)
                + "\n" + getApp().getResources().getString(R.string.gained_100_achievements_message,
                achievements);
        Assert.assertEquals(expectedText, shadowOf(dialog).getMessage());
    }

    @Test
    public void showAddLearntChordSuccessWithAchievements_ClickOkButton_CallsConfirmLearntSuccessOnPresenter() {
        // arrange
        int achievements = 2100;
        activity.showAddLearntChordSuccess(achievements);

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmLearntSuccess();
    }

    @Test
    public void showAddLearnChordSuccessWithLevelAndAchievements_ShowsAlertDialogWithSuccessMessage() {
        // act
        int achievements = 2000;
        int level = 3;
        activity.showAddLearntChordSuccess(level, achievements);

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        String expectedText = getApp().getResources().getString(R.string.add_learnt_chord_success_message)
                + "\n" + getApp().getResources().getString(R.string.gained_100_achievements_message,
                achievements) + "\n" + getApp().getResources().getString(R.string.new_level_message,
                level);
        Assert.assertEquals(expectedText, shadowOf(dialog).getMessage());
    }

    @Test
    public void showAddLearnChordSuccessWithLevelAndAchievements_ClickOkButton_CallsConfirmLearntSuccessOnPresenter() {
        // arrange
        int achievements = 2000;
        int level = 3;
        activity.showAddLearntChordSuccess(level, achievements);

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmLearntSuccess();
    }

    @Test
    public void showAddLearntChordError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showAddLearntChordError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.adding_learnt_chord_error_message),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void showVideoLoadError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showVideoLoadError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        junit.framework.Assert.assertEquals(getApp().getResources()
                        .getString(R.string.loading_chord_video_message_failure),
                shadowOf(dialog).getMessage());
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
    public void startDiagramHelpActivity_LearnDiagramHelpActivityIsStarted() {
        // act
        activity.startDiagramHelpActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LearnDiagramHelpActivity.class.getName(),
                intent.getComponent().getClassName());
    }

    @Test
    public void helpButtonClicked_CallsHelpRequestedOnPresenter() {
        // act
        Button btnHelp = (Button) activity.findViewById(R.id.btnHelp);
        btnHelp.performClick();

        // assert
        Mockito.verify(presenter).viewOnHelpRequested();
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

    @Test
    public void homeButtonClicked_StartsHomeActivityWithFlagsSetAndLearnChordActivityIsFinished() {
        // act
        Button btnHome = (Button) activity.findViewById(R.id.btnHome);
        btnHome.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        junit.framework.Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
        // check flags
        junit.framework.Assert.assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK,
                intent.getFlags());
        junit.framework.Assert.assertTrue(activity.isFinishing());
    }

    @Test
    public void learntButtonClicked_CallsLearntOnPresenter() {
        // act
        Button btnLearnt = (Button) activity.findViewById(R.id.btnLearnt);
        btnLearnt.performClick();

        // assert
        Mockito.verify(presenter).viewOnLearnt();
    }

    @Test
    public void enableLearntButtonWithFalse_DisablesLearntButton() {
        // act
        boolean isEnabled = false;
        activity.enableLearntButton(isEnabled);

        // assert
        Button btnLearnt = (Button) activity.findViewById(R.id.btnLearnt);
        Assert.assertEquals(isEnabled, btnLearnt.isEnabled());
    }

    @Test
    public void enableLearntButtonWithTrue_EnablesLearntButton() {
        // act
        boolean isEnabled = true;
        activity.enableLearntButton(isEnabled);

        // assert
        Button btnLearnt = (Button) activity.findViewById(R.id.btnLearnt);
        Assert.assertEquals(isEnabled, btnLearnt.isEnabled());
    }

    @Test
    public void showLearntConfirmDialog_ShowsAlertDialog() {
        // act
        activity.showLearntConfirmDialog();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        Assert.assertEquals(activity.getResources().getString(R.string.confirm_learnt_message),
                shadowOf(dialog).getMessage());
        Assert.assertEquals(activity.getResources().getString(R.string.yes),
                btnPositive.getText());
        Assert.assertEquals(activity.getResources().getString(R.string.no),
                btnNegative.getText());
        Assert.assertTrue(dialog.isShowing());
    }

    @Test
    public void showConfirmDialog_ClickYesButton_CallsConfirmLearntOnPresenter() {
        // arrange
        activity.showLearntConfirmDialog();

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnPositive.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmLearnt();
    }

    @Test
    public void finishActivity_FinishesActivity() {
        // act
        activity.finishActivity();

        // assert
        junit.framework.Assert.assertTrue(activity.isFinishing());
    }
}
