package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.views.LearnChordView;

import javax.inject.Inject;

/**
 * Activity that shows the details of a selected chord on screen
 */
public class LearnChordActivity extends BaseWithToolbarActivity implements LearnChordView {
    private ProgressDialog progressDialog;
    private Chord chord;
    private ILearnChordPresenter presenter;
    private boolean learntChord;

    @Override
    public int getLayout() {
        return R.layout.activity_chord;
    }

    @Override
    public String getToolbarTitle() {
        // no title on this screen
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieving selected chord
        chord = getIntent().getParcelableExtra("CHORD");

        // retrieving whether selected chord has been learned or not
        learntChord = getIntent().getBooleanExtra("LEARNT_CHORD", false);

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
                Intent intent = new Intent(getBaseContext(), LearnDiagramHelpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Inject
    public void setPresenter(ILearnChordPresenter presenter) {
        this.presenter = presenter;
        presenter.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        presenter.setView(this);
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(LearnChordActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chord_message));
        progressDialog.show();
    }

    @Override
    public Chord getChord() {
        return chord;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showImageLoadError() {
        Toast.makeText(getApplicationContext(),
                R.string.loading_chord_image_message_failure, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(),
                R.string.loading_chord_video_message_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean getLearntChord() {
        return learntChord;
    }

    @Override
    public void enableLearntButton(boolean isEnabled) {
        Button btnLearnt = (Button) findViewById(R.id.btnLearnt);
        btnLearnt.setEnabled(isEnabled);
    }

    @Override
    public void showConfirmDialog() {
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
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showAddLearntChordError() {
        Toast.makeText(getApplicationContext(),
                R.string.adding_learnt_chord_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddLearntChordSuccess() {
        String text = getString(R.string.add_learnt_chord_success_message) + "\n" +
                getString(R.string.maximum_achievements_message);

        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddLearntChordSuccess(int level, int achievements) {
        String text = getString(R.string.add_learnt_chord_success_message) + "\n" +
                getString(R.string.gained_100_achievements_message, achievements) + "\n" +
                getString(R.string.new_level_message, level);

        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddLearntChordSuccess(int achievements) {
        String text = getString(R.string.add_learnt_chord_success_message) + "\n" +
                getString(R.string.gained_100_achievements_message, achievements);

        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }
}
