package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.models.Song;

import java.util.List;

/**
 * Organising all songs into list with title, artist and chords
 */
public class SongsListAdapter extends ArrayAdapter<Song> {

    private final Context context;
    private final int layoutResource;

    public SongsListAdapter(Context context, int layoutResource, List<Song> songs) {
        super(context, layoutResource, songs);
        this.context = context;
        this.layoutResource = layoutResource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // instantiates layout xml
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutResource, null);
        }

        Song song = getItem(position);

        if (song != null) {
            TextView txtSongTitle = (TextView) convertView.findViewById(R.id.txtSongTitle);
            txtSongTitle.setText(song.getTitle());

            TextView txtSongArtist = (TextView) convertView.findViewById(R.id.txtSongArtist);
            txtSongArtist.setText(song.getArtist());

            TextView txtSongChords = (TextView) convertView.findViewById(R.id.txtSongChords);
            List<Chord> chords = song.getChords();
            String chordsList = "";

            // concatenating and formatting chords
            for (int i = 0; i < chords.size(); i++) {
                // if last in list, no comma at end
                if (i == chords.size() - 1) {
                    chordsList += chords.get(i).toString();
                } else {
                    chordsList += chords.get(i).toString() + ", ";
                }
            }
            txtSongChords.setText(chordsList);
        }

        return convertView;
    }
}
