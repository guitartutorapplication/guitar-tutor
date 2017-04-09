package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.jlo19.guitartutor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Organising displaying all chords in button form
 */
public class ChordsButtonAdapter extends BaseAdapter {

    private final List<Button> buttons;
    private final View.OnClickListener listener;
    private final Context context;

    public ChordsButtonAdapter(Context context, View.OnClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.buttons = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public Object getItem(int position) {
        return buttons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return buttons.get(position);
    }

    public void addButton(int id) {
        Button button = new Button(context);

        // setting button properties that aren't determined by its chord state
        button.setTextSize(context.getResources().getDimension(
                R.dimen.chord_button_text_size));
        button.setId(id);
        button.setOnClickListener(listener);
        button.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        // getting width/height of button in pixels to set
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float widthHeightDp = context.getResources().getDimension(
                R.dimen.chord_button_width_height);
        int widthHeightPx = (int) ((widthHeightDp * displayMetrics.density) + 0.5);
        button.setLayoutParams(new ViewGroup.LayoutParams(widthHeightPx, widthHeightPx));

        buttons.add(button);
    }

    public void setButtonText(int id, String text) {
        buttons.get(id).setText(text);
    }

    public void enableButton(int id, boolean isEnabled) {
        buttons.get(id).setEnabled(isEnabled);
    }

    public void setButtonBackground(int id, String doneIdentifier, String levelNumberIdentifier) {
        String backgroundIdentifier = "chord_" + doneIdentifier + "level_" + levelNumberIdentifier
                + "_button";

        buttons.get(id).setBackground(context.getDrawable(context.getResources().getIdentifier(
                backgroundIdentifier, "drawable", context.getPackageName())));
    }
}
