package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.AccountActivityListAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.views.AccountActivityView;

import java.util.List;

import javax.inject.Inject;

public class AccountActivityActivity extends BaseWithToolbarActivity implements AccountActivityView {

    private ProgressDialog progressDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_account_activity;
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.account_activity_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allows injection of presenter
        App.getComponent().inject(this);
    }

    @Inject
    public void setPresenter(IAccountActivityPresenter presenter) {
        presenter.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        presenter.setView(this);
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(AccountActivityActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_account_activity_message));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void setAccountActivity(List<Chord> chords) {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new AccountActivityListAdapter(AccountActivityActivity.this,
                R.layout.activity_list_item, chords));
    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(),
                R.string.loading_account_activity_message_failure, Toast.LENGTH_SHORT).show();
    }
}
