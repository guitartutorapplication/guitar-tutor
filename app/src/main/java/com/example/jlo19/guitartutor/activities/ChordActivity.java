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
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.ChordPresenter;
import com.example.jlo19.guitartutor.views.ChordView;

import java.net.URL;

import javax.inject.Inject;

/**
 * Activity that shows the details of a selected chord on screen
 */
public class ChordActivity extends AppCompatActivity implements ChordView {
    private ProgressDialog progressDialog;
    private Chord chord;
    private ChordPresenter presenter;

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
                presenter.getVideo();
            }
        });
        Button btnHelp = (Button) findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DiagramHelpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Inject
    public void setPresenter(ChordPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(ChordActivity.this, R.style.AppTheme_ProgressDialog);
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
    public void showError() {
        // error pop up message
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
    public void playVideo(URL url) {
        // passing URL to new activity
        Intent intent = new Intent(getBaseContext(), VideoActivity.class);
        intent.putExtra("URL", url.toString());
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
