package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.GridView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.ChordsButtonAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.views.LearnAllChordsView;

import javax.inject.Inject;


/**
 * Activity that shows all the chords on the screen
 */
public class LearnAllChordsActivity extends BaseWithToolbarActivity implements LearnAllChordsView {

    private ProgressDialog progressDialog;
    private ILearnAllChordsPresenter presenter;
    private ChordsButtonAdapter chordsButtonAdapter;
    private final int REQUEST_LEARNT = 1;

    @Override
    public int getLayout() {
        return R.layout.activity_all_chords;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.all_chords_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setChordsButtonAdapter(new ChordsButtonAdapter(LearnAllChordsActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnChordRequested(v.getId());
            }
        }));

        // allows injection of presenter
        App.getComponent().inject(this);
    }

    @Inject
    public void setPresenter(ILearnAllChordsPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    public void showError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = new AlertDialog.Builder(LearnAllChordsActivity.this)
                        .setMessage(R.string.loading_chords_message_failure)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.viewOnConfirmError();
                            }
                        }).create();
                dialog.show();

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(
                        LearnAllChordsActivity.this, R.color.colorAccent));
            }
        });
    }

    @Override
    public void startLearnChordActivity(Chord selectedChord, boolean learntChord) {
        // passing selected chord to new activity
        Intent intent = new Intent(getBaseContext(), LearnChordActivity.class);
        intent.putExtra("CHORD", selectedChord);
        intent.putExtra("LEARNT_CHORD", learntChord);
        startActivityForResult(intent, REQUEST_LEARNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LEARNT) {
            if (resultCode == RESULT_OK) {
                finishActivity();
            }
        }
    }

    @Override
    public void addChordButton(final int buttonId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chordsButtonAdapter.addButton(buttonId);
            }
        });
    }

    @Override
    public void setChordButtonText(final int buttonId, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chordsButtonAdapter.setButtonText(buttonId, text);
            }
        });
    }

    @Override
    public void enableChordButton(final int buttonId, final boolean isEnabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chordsButtonAdapter.enableButton(buttonId, isEnabled);
            }
        });
    }

    @Override
    public void setChordButtonBackground(final int buttonId, final String doneIdentifier, final
    String levelNumberIdentifier) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chordsButtonAdapter.setButtonBackground(buttonId, doneIdentifier, levelNumberIdentifier);
            }
        });
    }

    @Override
    public void setChordButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GridView gridView = (GridView) findViewById(R.id.gridView);
                gridView.setAdapter(chordsButtonAdapter);
            }
        });
    }

    @Override
    public void finishActivity() {
        finish();
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(LearnAllChordsActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chords_message));
        progressDialog.show();
    }

    public void hideProgressBar(){
        progressDialog.dismiss();
    }

    @VisibleForTesting
    void setChordsButtonAdapter(ChordsButtonAdapter adapter) {
        chordsButtonAdapter = adapter;
    }
}
