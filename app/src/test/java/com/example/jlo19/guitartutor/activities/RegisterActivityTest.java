package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;

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
 * Testing RegisterActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class RegisterActivityTest {

    private RegisterActivity activity;
    private IRegisterPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(RegisterActivity.class)
                .create().get();
        presenter = PowerMockito.mock(IRegisterPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void setTextFields_RegisterButtonClicked_CallsRegisterOnPresenter() {
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
        Button btnRegister = (Button) activity.findViewById(R.id.btnRegister);
        btnRegister.performClick();

        // assert
        Mockito.verify(presenter).viewOnRegister(expectedName, expectedEmail, expectedEmail,
                expectedPassword, expectedPassword);
    }

    @Test
    public void showFieldEmptyError_MakesToastWithFieldEmptyErrorMessage() {
        // act
        activity.showFieldEmptyError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showEmailMismatchError_MakesToastWithEmailMismatchErrorMessage() {
        // act
        activity.showEmailMismatchError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.field_email_mismatch_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordMismatchError_MakesToastWithPasswordMismatchErrorMessage() {
        // act
        activity.showPasswordMismatchError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.field_password_mismatch_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showInvalidEmailError_MakesToastWithInvalidEmailErrorMessage() {
        // act
        activity.showInvalidEmailError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.invalid_email_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordTooShortError_MakesToastWithPasswordTooShortErrorMessage() {
        // act
        activity.showPasswordTooShortError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.password_too_short_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordNoUpperCaseLetterError_MakesToastWithPasswordNoUpperCaseLetterErrorMessage() {
        // act
        activity.showPasswordNoUpperCaseLetterError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_upper_case_letter_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordNoLowerCaseLetterError_MakesToastWithPasswordNoLowerCaseLetterErrorMessage() {
        // act
        activity.showPasswordNoLowerCaseLetterError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_lower_case_letter_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showPasswordNoNumberError_MakesToastWithPasswordNoNumberErrorMessage() {
        // act
        activity.showPasswordNoNumberError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_number_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showRegisterError_MakesToastWithRegisterErrorMessage() {
        // act
        activity.showRegisterError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.register_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showAlreadyRegisteredError_MakesToastWithAlreadyRegisteredErrorMessage() {
        // act
        activity.showAlreadyRegisteredError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.already_registered_error_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void startLoginActivity_LoginActivityIsStarted() {
        // act
        activity.startLoginActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(LoginActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.registering_message),
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
}
