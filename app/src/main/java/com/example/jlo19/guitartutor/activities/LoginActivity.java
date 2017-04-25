package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.views.LoginView;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private ILoginPresenter presenter;
    private ProgressDialog progressDialog;
    private TextInputLayout txtInputPassword;
    private TextInputLayout txtInputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // allows injection of presenter
        App.getComponent().inject(this);

        TextView txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnRegister();
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
                EditText editTxtPassword = (EditText) findViewById(R.id.editTxtPassword);

                presenter.viewOnLogin(editTxtEmail.getText().toString(),
                        editTxtPassword.getText().toString());
            }
        });

        txtInputPassword = (TextInputLayout) findViewById(R.id.txtInputPassword);
        txtInputEmail = (TextInputLayout) findViewById(R.id.txtInputEmail);
    }

    @Inject
    public void setPresenter(ILoginPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public void startRegisterActivity() {
        Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.logging_in_message));
        progressDialog.show();
    }

    @Override
    public void showFieldEmailEmptyError() {
        txtInputEmail.setError(getResources().getString(R.string.field_empty_error_message));
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void startHomeActivity() {
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.login_error_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showFieldPasswordEmptyError() {
        txtInputPassword.setError(getResources().getString(R.string.field_empty_error_message));
    }

    @Override
    public void resetFieldEmptyErrors() {
        txtInputEmail.setError(null);
        txtInputPassword.setError(null);
    }
}
