package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowProgressDialog;

import static android.app.Activity.RESULT_OK;
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

        Assert.assertEquals(getApp().getResources().getString(R.string.field_email_mismatch_error_message),
                txtInputEmail.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.field_email_mismatch_error_message),
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

        Assert.assertEquals(getApp().getResources().getString(R.string.field_password_mismatch_error_message),
                txtInputPassword.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.field_password_mismatch_error_message),
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

        Assert.assertEquals(getApp().getResources().getString(R.string.invalid_email_error_message),
                txtInputEmail.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.invalid_email_error_message),
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

        Assert.assertEquals(getApp().getResources().getString(R.string.password_too_short_error_message),
                txtInputPassword.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.password_too_short_error_message),
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

        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_upper_case_letter_error_message),
                txtInputPassword.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_upper_case_letter_error_message),
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

        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_lower_case_letter_error_message),
                txtInputPassword.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_lower_case_letter_error_message),
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

        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_number_error_message),
                txtInputPassword.getError());
        Assert.assertEquals(getApp().getResources().getString(R.string.password_no_number_error_message),
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

        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.already_registered_error_message),
                txtInputEmail.getError());
        junit.framework.Assert.assertEquals(getApp().getResources().getString(R.string.already_registered_error_message),
                txtInputConfirmEmail.getError());
    }

    @Test
    public void showSaveError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showSaveError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        junit.framework.Assert.assertEquals(getApp().getResources()
                .getString(R.string.saving_changes_error_message),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void startAccountActivity_AccountActivityIsStartedWithResultSetAndEditAccountActivityIsFinished() {
        // act
        activity.startAccountActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        junit.framework.Assert.assertEquals(AccountActivity.class.getName(),
                intent.getComponent().getClassName());
        junit.framework.Assert.assertEquals(RESULT_OK, shadowOf(activity).getResultCode());
        Assert.assertTrue(activity.isFinishing());
    }

    @Test
    public void homeButtonClicked_StartsHomeActivityWithFlagsSetAndFinishesEditAccountActivity() {
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
    public void resetValidationErrors_SetsErrorToNullOnAllTextInputs() {
        // act
        activity.resetValidationErrors();

        // assert
        TextInputLayout txtInputName = (TextInputLayout) activity.findViewById(R.id.txtInputName);
        TextInputLayout txtInputEmail = (TextInputLayout) activity.findViewById(R.id.txtInputEmail);
        TextInputLayout txtInputConfirmEmail = (TextInputLayout) activity.findViewById(R.id.txtInputConfirmEmail);
        TextInputLayout txtInputPassword = (TextInputLayout) activity.findViewById(R.id.txtInputPassword);
        TextInputLayout txtInputConfirmPassword = (TextInputLayout) activity.findViewById(R.id.txtInputConfirmPassword);

        Assert.assertNull(txtInputName.getError());
        Assert.assertNull(txtInputEmail.getError());
        Assert.assertNull(txtInputConfirmEmail.getError());
        Assert.assertNull(txtInputPassword.getError());
        Assert.assertNull(txtInputConfirmPassword.getError());
    }
}
