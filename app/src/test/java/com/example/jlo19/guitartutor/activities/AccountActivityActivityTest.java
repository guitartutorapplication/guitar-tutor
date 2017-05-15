package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;

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

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing AccountActivityActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class AccountActivityActivityTest {

    private AccountActivityActivity activity;
    private IAccountActivityPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(AccountActivityActivity.class)
                .create().get();

        presenter = PowerMockito.mock(IAccountActivityPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void setsTitleOfToolbar() {
        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.account_activity_name),
                view.getText().toString());
    }

    @Test
    public void showError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_account_activity_message_failure),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void showError_ClickOkButton_CallsConfirmErrorOnPresenter() {
        // assert
        activity.showError();

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmError();
    }

    @Test
    public void finishActivity_FinishesActivity() {
        // act
        activity.finishActivity();

        // assert
        Assert.assertTrue(activity.isFinishing());
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_account_activity_message),
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
    public void homeButtonClicked_StartsHomeActivityWithFlagsSetAndFinishesAccountActivityActivity() {
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
    public void setAccountActivity_SetsListViewWithChordItems() {
        // act
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav" , 1));
        activity.setAccountActivity(chords);

        // assert
        ListView listView = (ListView) activity.findViewById(R.id.listView);
        Assert.assertEquals(chords.get(0), listView.getAdapter().getItem(0));
        Assert.assertEquals(chords.get(1), listView.getAdapter().getItem(1));
    }
}
