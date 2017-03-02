package com.example.jlo19.guitartutor.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.jlo19.guitartutor.API.GuitarTutorApi;
import com.example.jlo19.guitartutor.API.RetrofitClient;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.model.Chord;
import com.example.jlo19.guitartutor.model.ChordsResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChordActivity extends AppCompatActivity {
    AmazonS3 s3;
    String diagramFileName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord);

        // set chord ID
        final int chordId = getIntent().getIntExtra("CHORD_ID", 0);

        // show progress bar
        progressDialog = new ProgressDialog(ChordActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chord_message));
        progressDialog.show();

        // creates API
        GuitarTutorApi service = RetrofitClient.getClient().create(GuitarTutorApi.class);

        // gets chords from API
        Call<ChordsResponse> call = service.getChords();

        call.enqueue(new Callback<ChordsResponse>() {
            @Override
            public void onResponse(Call<ChordsResponse> call, Response<ChordsResponse> response) {
                List<Chord> chords = response.body().getChords();
                Chord chord = chords.get(chordId-1);
                diagramFileName = chord.getDiagramFilename();

                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        getApplicationContext(),
                        getResources().getString(R.string.identity_pool_id),
                        Regions.EU_WEST_1
                );

                s3 = new AmazonS3Client(credentialsProvider);
                s3.setRegion(com.amazonaws.regions.Region.getRegion(Regions.EU_WEST_1));
                new DownloadImageTask().execute();
            }

            @Override
            public void onFailure(Call<ChordsResponse> call, Throwable t) {
                progressDialog.hide();

                Toast.makeText(getApplicationContext(),
                        R.string.loading_chord_message_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {

            S3ObjectInputStream content = s3.getObject("guitar.tutor.data", diagramFileName)
                    .getObjectContent();
            byte[] bytes = new byte[0];
            try {
                bytes = IOUtils.toByteArray(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressDialog.hide();
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }
    }
}
