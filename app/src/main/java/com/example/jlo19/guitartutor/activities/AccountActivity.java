package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.views.AccountView;

import javax.inject.Inject;

/**
 * Activity that displays account details
 */
public class AccountActivity extends BaseWithToolbarActivity implements AccountView {

    private ProgressDialog progressDialog;
    private IAccountPresenter presenter;
    private final int REQUEST_SAVE = 1;

    @Override
    public int getLayout() {
        return R.layout.activity_account;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.account_details_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allows injection of presenter
        App.getComponent().inject(this);

        Button btnEditAccount = (Button) findViewById(R.id.btnEditAccount);
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnEditAccount();
            }
        });

        Button btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnLogout();
            }
        });

        Button btnActivity = (Button) findViewById(R.id.btnActivity);
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnAccountActivityRequested();
            }
        });
    }

    @Inject
    public void setPresenter(IAccountPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public void hideAccountButton() {
        Button btnAccount = (Button) findViewById(R.id.btnAccount);
        btnAccount.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setAccountDetails(String name, String email, int level, int achievements) {
        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        TextView txtLevel = (TextView) findViewById(R.id.txtLevel);
        TextView txtAchievements = (TextView) findViewById(R.id.txtAchievements);

        txtName.setText(name);
        txtEmail.setText(email);
        txtLevel.setText(String.valueOf(level));
        txtAchievements.setText(String.valueOf(achievements));
    }

    @Override
    public void showError() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.account_error_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmError();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void startEditAccountActivity(User user) {
        // passing account details to next activity
        Intent intent = new Intent(getBaseContext(), EditAccountActivity.class);
        intent.putExtra("USER", user);
        startActivityForResult(intent, REQUEST_SAVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SAVE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public void startAccountActivityActivity() {
        Intent intent = new Intent(getBaseContext(), AccountActivityActivity.class);
        startActivity(intent);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(AccountActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_account_details_message));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }
}
