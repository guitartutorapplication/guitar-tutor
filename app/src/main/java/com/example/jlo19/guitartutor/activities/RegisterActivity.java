package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.views.RegisterView;

import javax.inject.Inject;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private IRegisterPresenter presenter;
    private ProgressDialog progressDialog;

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
                presenter.viewOnRegister(editTxtName.getText().toString(),
                        editTxtEmail.getText().toString(),
                        editTxtConfirmEmail.getText().toString(),
                        editTxtPassword.getText().toString(),
                        editTxtConfirmPassword.getText().toString());
            }
        });
    }

    @Inject
    public void setPresenter(IRegisterPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public void showFieldEmptyError() {
        Toast.makeText(getApplicationContext(), R.string.field_empty_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailMismatchError() {
        Toast.makeText(getApplicationContext(), R.string.field_email_mismatch_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordMismatchError() {
        Toast.makeText(getApplicationContext(), R.string.field_password_mismatch_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInvalidEmailError() {
        Toast.makeText(getApplicationContext(), R.string.invalid_email_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordTooShortError() {
        Toast.makeText(getApplicationContext(), R.string.password_too_short_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordNoUpperCaseLetterError() {
        Toast.makeText(getApplicationContext(), R.string.password_no_upper_case_letter_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordNoLowerCaseLetterError() {
        Toast.makeText(getApplicationContext(), R.string.password_no_lower_case_letter_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordNoNumberError() {
        Toast.makeText(getApplicationContext(), R.string.password_no_number_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRegisterError() {
        Toast.makeText(getApplicationContext(), R.string.register_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlreadyRegisteredError() {
        Toast.makeText(getApplicationContext(), R.string.already_registered_error_message,
                Toast.LENGTH_SHORT).show();
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
