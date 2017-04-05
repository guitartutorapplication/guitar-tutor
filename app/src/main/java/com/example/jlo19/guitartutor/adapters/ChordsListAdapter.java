package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.Chord;

import java.util.ArrayList;
import java.util.List;

/**
 * Organising displaying chords in list with default option for spinner
 */
public class ChordsListAdapter extends BaseAdapter {

    private final List<Chord> chords;
    private final List<String> chordsTitle;
    private final LayoutInflater layoutInflater;

    public ChordsListAdapter(Context context, List<Chord> chords) {
        this.chords = chords;
        this.layoutInflater = LayoutInflater.from(context);

        // adding default option to list
        chordsTitle = new ArrayList<>();
        chordsTitle.add(context.getResources().getString(R.string.select_chord_instruction));
        for (Chord chord : chords) {
            chordsTitle.add(chord.toString());
        }
    }

    @Override
    public int getCount() {
        return chordsTitle.size();
    }

    @Override
    public Object getItem(int position) {
        // if default option, no chord object
        if (position == 0) {
            return null;
        }
        else {
            return chords.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.drop_down_item, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(chordsTitle.get(position));

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(android.R.layout.simple_spinner_item, null);
        }

        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(chordsTitle.get(position));

        return convertView;
    }
}
