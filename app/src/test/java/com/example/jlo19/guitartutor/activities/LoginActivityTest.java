package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowProgressDialog;

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
    public void showFieldEmailEmptyError_SetsErrorOnEmailTextInput() {
        // act
        activity.showFieldEmailEmptyError();

        // assert
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                txtInputEmail.getError());
    }

    @Test
    public void showFieldPasswordEmptyError_SetsErrorOnPasswordTextInput() {
        // act
        activity.showFieldPasswordEmptyError();

        // assert
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                txtInputPassword.getError());
    }

    @Test
    public void restFieldEmptyErrors_SetsErrorToNullOnTextInputs() {
        // act
        activity.resetFieldEmptyErrors();

        // assert
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);

        Assert.assertNull(txtInputEmail.getError());
        Assert.assertNull(txtInputPassword.getError());
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
    public void startHomeActivity_HomeActivityIsStartedAndLoginActivityIsFinished() {
        // act
        activity.startHomeActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
        Assert.assertTrue(activity.isFinishing());
    }

    @Test
    public void showLoginError_ShowsAlertDialogWithLoginErrorMessage() {
        // act
        activity.showLoginError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.login_error_message),
                shadowOf(dialog).getMessage());
    }
}
