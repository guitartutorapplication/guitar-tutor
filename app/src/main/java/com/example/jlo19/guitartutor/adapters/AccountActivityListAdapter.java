package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

/**
 * Organising account activity into list with each chord and num times it has been practised
 */
public class AccountActivityListAdapter extends ArrayAdapter<Chord> {

    private final Context context;
    private final int layoutResource;

    public AccountActivityListAdapter(Context context, int layoutResource, List<Chord> chords) {
        super(context, layoutResource, chords);
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

        Chord chord = getItem(position);

        if (chord != null) {
            TextView txtChord = (TextView) convertView.findViewById(R.id.txtChord);
            txtChord.setText(chord.toString());

            TextView txtPractiseNum = (TextView) convertView.findViewById(R.id.txtPractiseNum);
            txtPractiseNum.setText(String.valueOf(chord.getNumTimesPractised()));
        }

        return convertView;
    }
}
