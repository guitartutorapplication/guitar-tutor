package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.ChordPresenter;
import com.example.jlo19.guitartutor.views.ChordView;

import javax.inject.Inject;

/**
 * Activity that shows the details of a selected chord on screen
 */
public class ChordActivity extends AppCompatActivity implements ChordView {
    private ProgressDialog progressDialog;
    private Chord chord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord);

        // retrieving selected chord
        chord = getIntent().getParcelableExtra("CHORD");

        // allows injection of presenter
        ((App) getApplication())
                .getComponent()
                .inject(this);
    }

    @Inject
    public void setPresenter(ChordPresenter presenter) {
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
}
