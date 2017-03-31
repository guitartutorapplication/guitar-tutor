package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.retrofit.User;
import com.example.jlo19.guitartutor.presenters.EditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;

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
 * Testing EditAccountActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class EditAccountActivityTest {

    private EditAccountActivity activity;
    private User user;
    private IEditAccountPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        Intent intent = new Intent();
        intent.putExtra("USER", user);
        activity = Robolectric.buildActivity(EditAccountActivity.class, intent)
                .create().get();

        presenter = Mockito.mock(EditAccountPresenter.class);
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
    public void setsToolbarText() {
        // assert
        TextView textView = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(activity.getResources().getString(R.string.edit_account_details_name),
                textView.getText().toString());
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
    public void editTextsDisplayAccountDetailsFromIntent() {
        // assert
        EditText editTxtName = (EditText) activity.findViewById(R.id.editTxtName);
        EditText editTxtEmail = (EditText) activity.findViewById(R.id.editTxtEmail);
        EditText editTxtConfirmEmail = (EditText) activity.findViewById(R.id.editTxtConfirmEmail);

        Assert.assertEquals(user.getName(), editTxtName.getText().toString());
        Assert.assertEquals(user.getEmail(), editTxtEmail.getText().toString());
        Assert.assertEquals(user.getEmail(), editTxtConfirmEmail.getText().toString());
    }

    @Test
    public void setTextFields_SaveButtonClicked_CallsSaveOnPresenter() {
        // arrange
        String expectedName = "Kate";
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "password";

        EditText editTxtName = (EditText) activity.findViewById(R.id.editTxtName);
        editTxtName.setText(expectedName);

        EditText editTxtEmail = (EditText) activity.findViewById(R.id.editTxtEmail);
        editTxtEmail.setText(expectedEmail);

        EditText editTxtConfirmEmail = (EditText) activity.findViewById(R.id.editTxtConfirmEmail);
        editTxtConfirmEmail.setText(expectedEmail);

        EditText editTxtPassword = (EditText) activity.findViewById(R.id.editTxtPassword);
        editTxtPassword.setText(expectedPassword);

        EditText editTxtConfirmPassword = (EditText) activity.findViewById(R.id.editTxtConfirmPassword);
        editTxtConfirmPassword.setText(expectedPassword);

        // act
        Button btnSave = (Button) activity.findViewById(R.id.btnSave);
        btnSave.performClick();

        // assert
        Mockito.verify(presenter).viewOnSave(expectedName, expectedEmail, expectedEmail, expectedPassword,
                expectedPassword);
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        junit.framework.Assert.assertTrue(dialog.isShowing());
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.saving_changes_message),
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
    public void showFieldEmptyError_MakesToastWithFieldEmptyErrorMessage() {
        // act
        activity.showFieldEmptyError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showEmailMismatchError_MakesToastWithEmailMismatchErrorMessage() {
        // act
        activity.showEmailMismatchError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.field_email_mismatch_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordMismatchError_MakesToastWithPasswordMismatchErrorMessage() {
        // act
        activity.showPasswordMismatchError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.field_password_mismatch_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showInvalidEmailError_MakesToastWithInvalidEmailErrorMessage() {
        // act
        activity.showInvalidEmailError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.invalid_email_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordTooShortError_MakesToastWithPasswordTooShortErrorMessage() {
        // act
        activity.showPasswordTooShortError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.password_too_short_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordNoUpperCaseLetterError_MakesToastWithPasswordNoUpperCaseLetterErrorMessage() {
        // act
        activity.showPasswordNoUpperCaseLetterError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_upper_case_letter_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordNoLowerCaseLetterError_MakesToastWithPasswordNoLowerCaseLetterErrorMessage() {
        // act
        activity.showPasswordNoLowerCaseLetterError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_lower_case_letter_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordNoNumberError_MakesToastWithPasswordNoNumberErrorMessage() {
        // act
        activity.showPasswordNoNumberError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_number_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showSaveError_MakesToastWithSaveErrorMessage() {
        // act
        activity.showSaveError();

        // assert
        junit.framework.Assert.assertEquals(getApp().getResources()
                .getString(R.string.saving_changes_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void startAccountActivity_AccountActivityIsStarted() {
        // act
        activity.startAccountActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        junit.framework.Assert.assertEquals(AccountActivity.class.getName(),
                intent.getComponent().getClassName());
    }

    @Test
    public void homeButtonClicked_StartsHomeActivity() {
        // act
        Button btnHome = (Button) activity.findViewById(R.id.btnHome);
        btnHome.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        junit.framework.Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
    }
}
