package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.views.LearnChordView;

import javax.inject.Inject;

/**
 * Activity that shows the details of a selected chord on screen
 */
public class LearnChordActivity extends BaseWithToolbarActivity implements LearnChordView {

    private ProgressDialog progressDialog;
    private ILearnChordPresenter presenter;

    @Override
    public int getLayout() {
        return R.layout.activity_chord;
    }

    @Override
    public String getToolbarTitle() {
        // no title on this screen due to diagram displayed
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allows injection of presenter
        App.getComponent().inject(this);

        Button btnWatch = (Button) findViewById(R.id.btnWatch);
        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnVideoRequested();
            }
        });

        Button btnLearnt = (Button) findViewById(R.id.btnLearnt);
        btnLearnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnLearnt();
            }
        });

        Button btnHelp = (Button) findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnHelpRequested();
            }
        });
    }

    @Inject
    public void setPresenter(ILearnChordPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(LearnChordActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chord_message));
        progressDialog.show();
    }

    @Override
    public Chord getChord() {
        // returns chord selected in previous activity
        return getIntent().getParcelableExtra("CHORD");
    }

    @Override
    public void showImageLoadError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.loading_chord_image_message_failure)
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
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void setImage(Bitmap bitmap) {
        // setting chord diagram image
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void playVideo(String url) {
        // passing URL to new activity
        Intent intent = new Intent(getBaseContext(), LearnChordVideoActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

    @Override
    public void showVideoLoadError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.loading_chord_video_message_failure)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public boolean getLearntChord() {
        // returns whether chord is learnt or not
        return getIntent().getBooleanExtra("LEARNT_CHORD", false);
    }

    @Override
    public void enableLearntButton(boolean isEnabled) {
        Button btnLearnt = (Button) findViewById(R.id.btnLearnt);
        btnLearnt.setEnabled(isEnabled);
    }

    @Override
    public void showLearntConfirmDialog() {
        // asks user for confirmation that they understand chord
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_learnt_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmLearnt();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void startLearnAllChordsActivity() {
        Intent intent = new Intent(getBaseContext(), LearnAllChordsActivity.class);
        startActivity(intent);
        // returns ok result to parent activity
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void showAddLearntChordError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.adding_learnt_chord_error_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showAddLearntChordSuccess() {
        // displays success message with confirmation button
        String text = getString(R.string.add_learnt_chord_success_message) + "\n" +
                getString(R.string.maximum_achievements_message);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmLearntSuccess();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showAddLearntChordSuccess(int level, int achievements) {
        // displays success message with confirmation button
        String text = getString(R.string.add_learnt_chord_success_message) + "\n" +
                getString(R.string.gained_100_achievements_message, achievements) + "\n" +
                getString(R.string.new_level_message, level);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmLearntSuccess();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showAddLearntChordSuccess(int achievements) {
        // displays success message with confirmation button
        String text = getString(R.string.add_learnt_chord_success_message) + "\n" +
                getString(R.string.gained_100_achievements_message, achievements);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmLearntSuccess();
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

    @Override
    public void startDiagramHelpActivity() {
        Intent intent = new Intent(getBaseContext(), LearnDiagramHelpActivity.class);
        startActivity(intent);
    }
}
