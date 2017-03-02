package com.example.jlo19.guitartutor.adapter;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.jlo19.guitartutor.R;

/**
 * Organising displaying all chords in button form
 */

public class ChordsButtonAdapter extends BaseAdapter {

    private View.OnClickListener listener;
    private Context context;
    private String[] items;

    public ChordsButtonAdapter(Context context, String[] items, View.OnClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            button = new Button(context);
        }
        else {
            button = (Button) convertView;
        }
        button.setOnClickListener(listener);
        button.setText(items[position]);
        button.setId(position+1);

        return button;
    }
}
