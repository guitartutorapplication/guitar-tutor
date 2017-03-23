package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.SongsListAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.retrofit.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.views.SongLibraryView;

import java.util.List;

import javax.inject.Inject;

/**
 * Activity that displays all songs
 */
public class SongLibraryActivity extends AppCompatActivity implements SongLibraryView {

    private ProgressDialog progressDialog;
    private ListView listView;
    private List<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_library);

        // allows injection of presenter
        App.getComponent().inject(this);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // passing selected song to new activity
                Intent intent = new Intent(getBaseContext(), SongActivity.class);
                intent.putExtra("SONG", songs.get(position));
                startActivity(intent);
            }
        });
    }

    @Inject
    public void setPresenter(ISongLibraryPresenter presenter) {
        presenter.setView(this);
    }

    @Override
    public void setToolbarTitleText() {
        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(R.string.song_library_name);
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(SongLibraryActivity.this, R.style.AppTheme_ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading_songs_message));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void setSongs(List<Song> songs) {
        this.songs = songs;
        listView.setAdapter(new SongsListAdapter(SongLibraryActivity.this, R.layout.song_list_item, songs));
    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(),
                R.string.loading_songs_message_failure, Toast.LENGTH_SHORT).show();
    }
}
