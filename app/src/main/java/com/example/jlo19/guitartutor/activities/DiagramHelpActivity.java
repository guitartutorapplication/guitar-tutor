package com.example.jlo19.guitartutor.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;

public class DiagramHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram_help);

        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(R.string.diagram_help_name);
    }
}
