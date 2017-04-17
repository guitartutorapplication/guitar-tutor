package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private User user;
    private IAccountPresenter presenter;

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
        presenter.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        presenter.setView(this);
    }

    @Override
    public void hideAccountButton() {
        Button btnAccount = (Button) findViewById(R.id.btnAccount);
        btnAccount.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setAccountDetails(User user) {
        this.user = user;
        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        TextView txtLevel = (TextView) findViewById(R.id.txtLevel);
        TextView txtAchievements = (TextView) findViewById(R.id.txtAchievements);

        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtLevel.setText(String.valueOf(user.getLevel()));
        txtAchievements.setText(String.valueOf(user.getAchievements()));
    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(), R.string.account_error_message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void startEditAccountActivity() {
        // passing account details to next activity
        Intent intent = new Intent(getBaseContext(), EditAccountActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    @Override
    public void startAccountActivityActivity() {
        Intent intent = new Intent(getBaseContext(), AccountActivityActivity.class);
        startActivity(intent);
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
