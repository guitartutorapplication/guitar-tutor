package com.example.jlo19.guitartutor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.API.GuitarTutorApi;
import com.example.jlo19.guitartutor.API.RetrofitClient;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapter.ChordsButtonAdapter;
import com.example.jlo19.guitartutor.model.Chord;
import com.example.jlo19.guitartutor.model.ChordsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllChordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chords);

        TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText(R.string.all_chords_name);

        // show progress bar
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(AllChordsActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_chords_message));
        progressDialog.show();

        // creates API
        GuitarTutorApi service = RetrofitClient.getClient().create(GuitarTutorApi.class);

        // gets chords from API
        Call<ChordsResponse> call = service.getChords();

        call.enqueue(new Callback<ChordsResponse>() {
            @Override
            public void onResponse(Call<ChordsResponse> call, Response<ChordsResponse> response) {
                progressDialog.hide();
                List<Chord> chords = response.body().getChords();
                GridView gridView = (GridView) findViewById(R.id.gridView);

                String[] chordNames = new String[chords.size()];
                 for (int i = 0; i < chords.size(); i++) {
                     chordNames[i] = chords.get(i).toString();
                }

                 gridView.setAdapter(new ChordsButtonAdapter(AllChordsActivity.this, chordNames,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int chordId = v.getId();
                                Intent intent = new Intent(getBaseContext(), ChordActivity.class);
                                intent.putExtra("CHORD_ID", chordId);
                                startActivity(intent);
                            }
                }));
            }

            @Override
            public void onFailure(Call<ChordsResponse> call, Throwable t) {
                progressDialog.hide();

                Toast.makeText(getApplicationContext(),
                        R.string.loading_chords_message_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
