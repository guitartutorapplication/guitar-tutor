package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.ChordsButtonAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;

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
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing LearnAllChordsActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class LearnAllChordsActivityTest {

    private LearnAllChordsActivity activity;
    private ILearnAllChordsPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(LearnAllChordsActivity.class)
                .create().get();
        presenter = PowerMockito.mock(ILearnAllChordsPresenter.class);
        activity.setPresenter(presenter);

        activity.setChordButtons();
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void setPresenter_SetsSharedPreferencesOnPresenter() {
        // assert
        Mockito.verify(presenter).setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(activity));
    }

    @Test
    public void showError_MakesToastWithErrorMessage() {
        // act
        activity.showError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_chords_message_failure),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void setsTitleOfToolbar() {
        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.all_chords_name),
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

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_chords_message),
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
    public void addButton_ClickButton_CallsChordRequestedOnPresenterWithId() {
        // arrange
        int id = 0;
        activity.addChordButton(id);

        // act
        Button chordButton = (Button) ((GridView) activity.findViewById(R.id.gridView))
                .getAdapter().getView(id, null, null);
        chordButton.performClick();

        // assert
        Mockito.verify(presenter).viewOnChordRequested(id);
    }

    @Test
    public void startLearnChordActivity_LearnChordActivityIsStarted() {
        // act
        Chord chord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1);
        boolean isLearntChord = true;
        activity.startLearnChordActivity(chord, true);

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LearnChordActivity.class.getName(), intent.getComponent().getClassName());
        // checks correct chord is passed through
        Assert.assertEquals(chord, intent.getParcelableExtra("CHORD"));
        // checks if learnt is passed through correctly
        Assert.assertEquals(isLearntChord, intent.getBooleanExtra("LEARNT_CHORD", false));
    }

    @Test
    public void addChordButton_CallsAddButtonOnAdapter() {
        // arrange
        ChordsButtonAdapter adapter = Mockito.mock(ChordsButtonAdapter.class);
        activity.setChordsButtonAdapter(adapter);

        // act
        int id = 0;
        activity.addChordButton(id);

        // assert
        Mockito.verify(adapter).addButton(id);
    }

    @Test
    public void setChordButtonText_CallsSetButtonTextOnAdapter() {
        // arrange
        ChordsButtonAdapter adapter = Mockito.mock(ChordsButtonAdapter.class);
        activity.setChordsButtonAdapter(adapter);

        // act
        int id = 0;
        String text = "A";
        activity.setChordButtonText(id, text);

        // assert
        Mockito.verify(adapter).setButtonText(id, text);
    }

    @Test
    public void enabledChordButton_CallsEnableButtonOnAdapter() {
        // arrange
        ChordsButtonAdapter adapter = Mockito.mock(ChordsButtonAdapter.class);
        activity.setChordsButtonAdapter(adapter);

        // act
        int id = 0;
        boolean isEnabled = true;
        activity.enableChordButton(id, isEnabled);

        // assert
        Mockito.verify(adapter).enableButton(id, isEnabled);
    }

    @Test
    public void setChordButtonBackground_CallsSetButtonBackgroundOnAdapter() {
        // arrange
        ChordsButtonAdapter adapter = Mockito.mock(ChordsButtonAdapter.class);
        activity.setChordsButtonAdapter(adapter);

        // act
        int id = 0;
        String doneIdentifier = "done_";
        String numberIdentifier = "one";
        activity.setChordButtonBackground(id, doneIdentifier, numberIdentifier);

        // assert
        Mockito.verify(adapter).setButtonBackground(id, doneIdentifier, numberIdentifier);
    }
}