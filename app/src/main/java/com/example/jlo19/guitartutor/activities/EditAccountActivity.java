package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.views.EditAccountView;

import javax.inject.Inject;

public class EditAccountActivity extends BaseWithToolbarActivity implements EditAccountView {

    private IEditAccountPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_edit_account;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.edit_account_details_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allows injection of presenter
        App.getComponent().inject(this);

        // retrieving account details and displaying in edittext
        User user = getIntent().getParcelableExtra("USER");

        final EditText editTxtName = (EditText) findViewById(R.id.editTxtName);
        editTxtName.setText(user.getName());

        final EditText editTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        editTxtEmail.setText(user.getEmail());

        final EditText editTxtConfirmEmail = (EditText) findViewById(R.id.editTxtConfirmEmail);
        editTxtConfirmEmail.setText(user.getEmail());

        final EditText editTxtPassword = (EditText) findViewById(R.id.editTxtPassword);

        final EditText editTxtConfirmPassword = (EditText) findViewById(R.id.editTxtConfirmPassword);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnSave(editTxtName.getText().toString(),
                        editTxtEmail.getText().toString(),
                        editTxtConfirmEmail.getText().toString(),
                        editTxtPassword.getText().toString(),
                        editTxtConfirmPassword.getText().toString());
            }
        });
    }

    @Inject
    public void setPresenter(IEditAccountPresenter presenter) {
        this.presenter = presenter;
        presenter.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        presenter.setView(this);
    }

    public void hideAccountButton() {
        Button btnAccount = (Button) findViewById(R.id.btnAccount);
        btnAccount.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSaveError() {
        Toast.makeText(getApplicationContext(), R.string.saving_changes_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startAccountActivity() {
        Intent intent = new Intent(getBaseContext(), AccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(EditAccountActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.saving_changes_message));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
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
    public void showAlreadyRegisteredError() {
        Toast.makeText(getApplicationContext(), R.string.already_registered_error_message,
                Toast.LENGTH_SHORT).show();
    }
}
