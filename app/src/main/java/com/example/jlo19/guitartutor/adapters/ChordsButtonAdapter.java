package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.Chord;

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
        button.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        button.setBackground(context.getDrawable(R.drawable.chord_button));

        float textSize = context.getResources().getDimension(
                R.dimen.chord_button_text_size);
        button.setTextSize(textSize);

        // getting width/height of button in pixels to set
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float widthHeightDp = context.getResources().getDimension(
                R.dimen.chord_button_width_height);
        int widthHeightPx = (int) ((widthHeightDp * displayMetrics.density) + 0.5);
        button.setLayoutParams(new ViewGroup.LayoutParams(widthHeightPx, widthHeightPx));

        return button;
    }
}
