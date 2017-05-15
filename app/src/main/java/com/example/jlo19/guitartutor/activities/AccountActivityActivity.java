package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.AccountActivityListAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.views.AccountActivityView;

import java.util.List;

import javax.inject.Inject;

/**
 * Activity that displays account activity (learnt chords and how many times each has been practised)
 */
public class AccountActivityActivity extends BaseWithToolbarActivity implements AccountActivityView {

    private ProgressDialog progressDialog;
    private IAccountActivityPresenter presenter;

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
        this.presenter = presenter;
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
        // take account activity (chords with num times practised) and display in list
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new AccountActivityListAdapter(AccountActivityActivity.this,
                R.layout.activity_list_item, chords));
    }

    @Override
    public void showError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.loading_account_activity_message_failure)
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
    public void finishActivity() {
        finish();
    }
}
