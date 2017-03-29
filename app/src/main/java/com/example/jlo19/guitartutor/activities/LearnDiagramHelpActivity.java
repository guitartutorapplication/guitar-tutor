package com.example.jlo19.guitartutor.activities;

import android.content.res.Configuration;
import android.os.Bundle;

import com.example.jlo19.guitartutor.R;

/**
 * Activity that shows diagram help
 */
public class LearnDiagramHelpActivity extends BaseWithToolbarActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_diagram_help;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.diagram_help_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
