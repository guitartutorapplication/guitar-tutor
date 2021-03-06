package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.adapters.SongsListAdapter;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.views.SongLibraryView;

import java.util.List;

import javax.inject.Inject;

/**
 * Activity that displays all songs
 */
public class SongLibraryActivity extends BaseWithToolbarActivity implements SongLibraryView {

    private ProgressDialog progressDialog;
    private ListView listView;
    private ISongLibraryPresenter presenter;
    private List<Song> songs;

    @Override
    public int getLayout() {
        return R.layout.activity_song_library;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.song_library_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // allows injection of presenter
        App.getComponent().inject(this);

        RadioGroup rGroupViewFilter = (RadioGroup) findViewById(R.id.rGroupViewFilter);
        rGroupViewFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                presenter.viewOnSongFilterChanged(checkedId == R.id.rbtnViewAll);
            }
        });
    }

    @Inject
    public void setPresenter(ISongLibraryPresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
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
        // displays all songs in list
        this.songs = songs;
        listView.setAdapter(new SongsListAdapter(SongLibraryActivity.this, R.layout.song_list_item, songs));
    }

    @Override
    public void showError() {
        // displays error message with confirmation button
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.loading_songs_message_failure)
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.viewOnExit();
    }
}
