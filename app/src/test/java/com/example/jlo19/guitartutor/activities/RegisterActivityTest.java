package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowProgressDialog;

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
    public void showFieldEmptyNameError_SetsNameTextInputWithMessage() {
        // act
        activity.showFieldEmptyNameError();

        // assert
        TextInputLayout txtInputName = (TextInputLayout) activity.findViewById(R.id.txtInputName);
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                txtInputName.getError());
    }

    @Test
    public void showFieldEmptyEmailError_SetsEmailTextInputWithMessage() {
        // act
        activity.showFieldEmptyEmailError();

        // assert
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                txtInputEmail.getError());
    }

    @Test
    public void showFieldEmptyConfirmEmailError_SetsConfirmEmailTextInputWithMessage() {
        // act
        activity.showFieldEmptyConfirmEmailError();

        // assert
        TextInputLayout txtInputConfirmEmail = (TextInputLayout) activity.findViewById(R.id.txtInputConfirmEmail);
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                txtInputConfirmEmail.getError());
    }

    @Test
    public void showFieldEmptyPasswordError_SetsPasswordTextInputWithMessage() {
        // act
        activity.showFieldEmptyPasswordError();

        // assert
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                txtInputPassword.getError());
    }

    @Test
    public void showFieldEmptyConfirmPasswordError_SetsConfirmPasswordTextInputWithMessage() {
        // act
        activity.showFieldEmptyConfirmPasswordError();

        // assert
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(R.id.txtInputConfirmPassword);
        Assert.assertEquals(getApp().getResources().getString(R.string.field_empty_error_message),
                txtInputConfirmPassword.getError());
    }

    @Test
    public void showEmailMismatchError_SetErrorOnEmailAndConfirmEmailTextInputWithMessage() {
        // act
        activity.showEmailMismatchError();

        // assert
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        TextInputLayout txtInputConfirmEmail = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmEmail);

        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.field_email_mismatch_error_message),
                txtInputEmail.getError());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.field_email_mismatch_error_message),
                txtInputConfirmEmail.getError());
    }

    @Test
    public void showPasswordMismatchError_SetErrorOnPasswordAndConfirmPasswordTextInputWithMessage() {
        // act
        activity.showPasswordMismatchError();

        // assert
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmPassword);

        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.field_password_mismatch_error_message),
                txtInputPassword.getError());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.field_password_mismatch_error_message),
                txtInputConfirmPassword.getError());
    }

    @Test
    public void showInvalidEmailError_SetErrorOnEmailAndConfirmEmailTextInputWithMessage() {
        // act
        activity.showInvalidEmailError();

        // assert
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        TextInputLayout txtInputConfirmEmail = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmEmail);

        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.invalid_email_error_message),
                txtInputEmail.getError());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.invalid_email_error_message),
                txtInputConfirmEmail.getError());
    }

    @Test
    public void showPasswordTooShortError_SetErrorOnPasswordAndConfirmPasswordTextInputWithMessage() {
        // act
        activity.showPasswordTooShortError();

        // assert
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmPassword);

        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_too_short_error_message),
                txtInputPassword.getError());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_too_short_error_message),
                txtInputConfirmPassword.getError());
    }

    @Test
    public void showPasswordNoUpperCaseLetterError_SetErrorOnPasswordAndConfirmPasswordTextInputWithMessage() {
        // act
        activity.showPasswordNoUpperCaseLetterError();

        // assert
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmPassword);

        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_upper_case_letter_error_message),
                txtInputPassword.getError());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_upper_case_letter_error_message),
                txtInputConfirmPassword.getError());
    }

    @Test
    public void showPasswordNoLowerCaseLetterError_SetErrorOnPasswordAndConfirmPasswordTextInputWithMessage() {
        // act
        activity.showPasswordNoLowerCaseLetterError();

        // assert
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmPassword);

        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_lower_case_letter_error_message),
                txtInputPassword.getError());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_lower_case_letter_error_message),
                txtInputConfirmPassword.getError());
    }

    @Test
    public void showPasswordNoNumberError_SetErrorOnPasswordAndConfirmPasswordTextInputWithMessage() {
        // act
        activity.showPasswordNoNumberError();

        // assert
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmPassword);

        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_number_error_message),
                txtInputPassword.getError());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.password_no_number_error_message),
                txtInputConfirmPassword.getError());
    }

    @Test
    public void showAlreadyRegisteredError_SetErrorOnEmailAndConfirmEmailTextInputWithMessage() {
        // act
        activity.showAlreadyRegisteredError();

        // assert
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        TextInputLayout txtInputConfirmEmail = (TextInputLayout) activity.findViewById(
                R.id.txtInputConfirmEmail);

        Assert.assertEquals(getApp().getResources().getString(R.string.already_registered_error_message),
                txtInputEmail.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.already_registered_error_message),
                txtInputConfirmEmail.getError());
    }

    @Test
    public void showRegisterError_ShowsAlertDialogWithRegisterErrorMessage() {
        // act
        activity.showRegisterError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.register_error_message),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void finishRegister_ActivityIsFinished() {
        // act
        activity.finishRegister();

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

    @Test
    public void resetValidationErrors_SetsErrorToNullOnAllTextInputs() {
        // act
        activity.resetValidationErrors();

        // assert
        TextInputLayout txtInputName = (TextInputLayout) activity.findViewById(R.id.txtInputName);
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        TextInputLayout txtInputConfirmEmail = (TextInputLayout) activity.findViewById(R.id.txtInputConfirmEmail);
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(R.id.txtInputConfirmPassword);

        org.junit.Assert.assertNull(txtInputName.getError());
        org.junit.Assert.assertNull(txtInputEmail.getError());
        org.junit.Assert.assertNull(txtInputConfirmEmail.getError());
        org.junit.Assert.assertNull(txtInputPassword.getError());
        org.junit.Assert.assertNull(txtInputConfirmPassword.getError());
    }
}
