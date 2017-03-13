package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.views.LearnChordView;

import javax.inject.Inject;

/**
 * Activity that shows the details of a selected chord on screen
 */
public class LearnChordActivity extends AppCompatActivity implements LearnChordView {
    private ProgressDialog progressDialog;
    private Chord chord;
    private ILearnChordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord);

        // retrieving selected chord
        chord = getIntent().getParcelableExtra("CHORD");

        // allows injection of presenter
        App.getComponent().inject(this);

        Button btnWatch = (Button) findViewById(R.id.btnWatch);
        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.viewOnVideoRequested();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
