package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.views.LoginView;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private ILoginPresenter presenter;
    private ProgressDialog progressDialog;

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
    public void showFieldEmptyError() {
        Toast.makeText(getApplicationContext(), R.string.field_empty_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void startHomeActivity() {
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void showIncorrectCredentialsError() {
        Toast.makeText(getApplicationContext(), R.string.incorrect_login_credentials_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginError() {
        Toast.makeText(getApplicationContext(), R.string.login_error_message,
                Toast.LENGTH_SHORT).show();
    }
}
