package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.jlo19.guitartutor.models.Chord;

import java.util.List;

/**
 * Organising displaying all chords in button form
 */
public class ChordsButtonAdapter extends BaseAdapter {

    private List<Chord> chords;
    private View.OnClickListener listener;
    private Context context;

    public ChordsButtonAdapter(Context context, List<Chord> chords, View.OnClickListener listener) {
        this.context = context;
        this.chords = chords;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return chords.size();
    }

    @Override
    public Object getItem(int position) {
        return chords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            button = new Button(context);
        }
        else {
            button = (Button) convertView;
        }
        button.setOnClickListener(listener);
        button.setText(chords.get(position).toString());
        button.setId(position);

        return button;
    }
}
