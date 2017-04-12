package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.AccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;

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
 * Testing AccountActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class AccountActivityTest {

    private AccountActivity activity;
    private IAccountPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(AccountActivity.class)
                .create().get();

        presenter = PowerMockito.mock(AccountPresenter.class);
        activity.setPresenter(presenter);
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
    public void setsTitleOfToolbar() {
        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.account_details_name),
                view.getText().toString());
    }

    @Test
    public void hideAccountButton_SetsVisibilityOptionOnAccountButtonToInvisible() {
        // act
        activity.hideAccountButton();

        // assert
        Button btnAccount = (Button) activity.findViewById(R.id.btnAccount);
        int actualVisibility = btnAccount.getVisibility();
        Assert.assertEquals(View.INVISIBLE, actualVisibility);
    }

    @Test
    public void setAccountDetails_SetsTextViews() {
        // act
        String expectedName = "Kate";
        String expectedEmail = "katesmith@gmail.com";
        int expectedLevel = 2;
        int expectedAchievements = 2000;
        activity.setAccountDetails(new User(expectedName, expectedEmail, expectedLevel,
                expectedAchievements));

        // assert
        TextView txtName = (TextView) activity.findViewById(R.id.txtName);
        TextView txtEmail = (TextView) activity.findViewById(R.id.txtEmail);
        TextView txtLevel = (TextView) activity.findViewById(R.id.txtLevel);
        TextView txtAchievements = (TextView) activity.findViewById(R.id.txtAchievements);

        Assert.assertEquals(expectedName, txtName.getText().toString());
        Assert.assertEquals(expectedEmail, txtEmail.getText().toString());
        Assert.assertEquals(String.valueOf(expectedLevel), txtLevel.getText().toString());
        Assert.assertEquals(String.valueOf(expectedAchievements), txtAchievements.getText().toString());
    }

    @Test
    public void showError_MakesToastWithErrorMessage() {
        // act
        activity.showError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.account_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_account_details_message),
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
    public void startEditAccountActivity_EditAccountActivityIsStartedWithAccountDetails() {
        // arrange
        User user = new User("Kate", "katesmith@gmail.com", 2,
                2000);
        activity.setAccountDetails(user);

        // act
        activity.startEditAccountActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(EditAccountActivity.class.getName(), intent.getComponent().getClassName());
        // checks correct chord is passed through
        Assert.assertEquals(user, intent.getParcelableExtra("USER"));
    }

    @Test
    public void editAccountButtonClicked_CallsEditAccountOnPresenter() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnEditAccount);
        button.performClick();

        // assert
        Mockito.verify(presenter).viewOnEditAccount();
    }

    @Test
    public void logOutButtonClicked_CallsLogoutOnPresenter() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnLogOut);
        button.performClick();

        // assert
        Mockito.verify(presenter).viewOnLogout();
    }

    @Test
    public void startLoginActivity_LoginActivityIsStartedWithFlagsSet() {
        // act
        activity.startLoginActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LoginActivity.class.getName(), intent.getComponent().getClassName());
        // check flags
        Assert.assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK,
                intent.getFlags());
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
