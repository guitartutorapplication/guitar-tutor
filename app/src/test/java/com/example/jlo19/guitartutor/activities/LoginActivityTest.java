package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.presenters.LoginPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;

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
 * Testing LoginActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    private LoginActivity activity;
    private ILoginPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp() {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(LoginActivity.class)
                .create().get();

        presenter = PowerMockito.mock(LoginPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void clickRegister_CallsRegisterOnPresenter() {
        // act
        TextView txtRegister = (TextView) activity.findViewById(R.id.txtRegister);
        txtRegister.performClick();

        // assert
        Mockito.verify(presenter).viewOnRegister();
    }

    @Test
    public void setTextFields_ClickLogin_CallsLoginOnPresenter() {
        // arrange
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "password";

        EditText editTxtEmail = (EditText) activity.findViewById(R.id.editTxtEmail);
        editTxtEmail.setText(expectedEmail);

        EditText editTxtPassword = (EditText) activity.findViewById(R.id.editTxtPassword);
        editTxtPassword.setText(expectedPassword);

        // act
        Button btnLogin = (Button) activity.findViewById(R.id.btnLogin);
        btnLogin.performClick();

        // assert
        Mockito.verify(presenter).viewOnLogin(expectedEmail, expectedPassword);
    }

    @Test
    public void startRegisterActivity_RegisterActivityIsStarted() {
        // act
        activity.startRegisterActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(RegisterActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.logging_in_message),
                (shadowOf(dialog).getMessage()));
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
    public void startHomeActivity_HomeActivityIsStarted() {
        // act
        activity.startHomeActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void showIncorrectCredentialsError_MakesToastWithIncorrectCredentialsErrorMessage() {
        // act
        activity.showIncorrectCredentialsError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(
                R.string.incorrect_login_credentials_error_message), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showLoginError_MakesToastWithGeneralLoginErrorMessage() {
        // act
        activity.showLoginError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.login_error_message),
                ShadowToast.getTextOfLatestToast());
    }
}
