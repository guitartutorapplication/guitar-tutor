package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.views.RegisterView;

import javax.inject.Inject;

/**
 * Activity that allows user to register for an account
 */
public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private IRegisterPresenter presenter;
    private ProgressDialog progressDialog;
    private TextInputLayout txtInputName;
    private TextInputLayout txtInputEmail;
    private TextInputLayout txtInputConfirmEmail;
    private TextInputLayout txtInputPassword;
    private TextInputLayout txtInputConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // allows injection of presenter
        App.getComponent().inject(this);

        final EditText editTxtName = (EditText) findViewById(R.id.editTxtName);
        final EditText editTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        final EditText editTxtConfirmEmail = (EditText) findViewById(R.id.editTxtConfirmEmail);
        final EditText editTxtPassword = (EditText) findViewById(R.id.editTxtPassword);
        final EditText editTxtConfirmPassword = (EditText) findViewById(R.id.editTxtConfirmPassword);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing details from edittext to register with
                presenter.viewOnRegister(editTxtName.getText().toString(),
                        editTxtEmail.getText().toString(),
                        editTxtConfirmEmail.getText().toString(),
                        editTxtPassword.getText().toString(),
                        editTxtConfirmPassword.getText().toString());
            }
        });

        txtInputName = (TextInputLayout) findViewById(R.id.txtInputName);
        txtInputEmail = (TextInputLayout) findViewById(R.id.txtInputEmail);
        txtInputConfirmEmail = (TextInputLayout) findViewById(R.id.txtInputConfirmEmail);
        txtInputPassword = (TextInputLayout) findViewById(R.id.txtInputPassword);
        txtInputConfirmPassword = (TextInputLayout) findViewById(R.id.txtInputConfirmPassword);
    }

    @Inject
    public void setPresenter(IRegisterPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public void resetValidationErrors() {
        txtInputName.setError(null);
        txtInputEmail.setError(null);
        txtInputConfirmEmail.setError(null);
        txtInputPassword.setError(null);
        txtInputConfirmPassword.setError(null);
    }

    @Override
    public void showFieldEmptyNameError() {
        showFieldEmptyError(txtInputName);
    }

    @Override
    public void showFieldEmptyEmailError() {
        showFieldEmptyError(txtInputEmail);
    }

    @Override
    public void showFieldEmptyConfirmEmailError() {
        showFieldEmptyError(txtInputConfirmEmail);
    }

    @Override
    public void showFieldEmptyPasswordError() {
        showFieldEmptyError(txtInputPassword);
    }

    @Override
    public void showFieldEmptyConfirmPasswordError() {
        showFieldEmptyError(txtInputConfirmPassword);
    }

    private void showFieldEmptyError(TextInputLayout txtInput) {
        String error = getResources().getString(R.string.field_empty_error_message);
        txtInput.setError(error);
    }

    @Override
    public void showEmailMismatchError() {
        String error = getResources().getString(R.string.field_email_mismatch_error_message);
        txtInputEmail.setError(error);
        txtInputConfirmEmail.setError(error);
    }

    @Override
    public void showPasswordMismatchError() {
        String error = getResources().getString(R.string.field_password_mismatch_error_message);
        txtInputPassword.setError(error);
        txtInputConfirmPassword.setError(error);
    }

    @Override
    public void showInvalidEmailError() {
        String error = getResources().getString(R.string.invalid_email_error_message);
        txtInputEmail.setError(error);
        txtInputConfirmEmail.setError(error);
    }

    @Override
    public void showPasswordTooShortError() {
        String error = getResources().getString(R.string.password_too_short_error_message);
        txtInputPassword.setError(error);
        txtInputConfirmPassword.setError(error);
    }

    @Override
    public void showPasswordNoUpperCaseLetterError() {
        String error = getResources().getString(R.string.password_no_upper_case_letter_error_message);
        txtInputPassword.setError(error);
        txtInputConfirmPassword.setError(error);
    }

    @Override
    public void showPasswordNoLowerCaseLetterError() {
        String error = getResources().getString(R.string.password_no_lower_case_letter_error_message);
        txtInputPassword.setError(error);
        txtInputConfirmPassword.setError(error);
    }

    @Override
    public void showPasswordNoNumberError() {
        String error = getResources().getString(R.string.password_no_number_error_message);
        txtInputPassword.setError(error);
        txtInputConfirmPassword.setError(error);
    }

    @Override
    public void showAlreadyRegisteredError() {
        String error = getResources().getString(R.string.already_registered_error_message);
        txtInputEmail.setError(error);
        txtInputConfirmEmail.setError(error);
    }

    @Override
    public void showRegisterError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.register_error_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void finishRegister() {
        finish();
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.registering_message));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }
}
