package com.example.jlo19.guitartutor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;

/**
 * Abstract class that deals with all toolbar behaviour
 */
public abstract class BaseWithToolbarActivity extends AppCompatActivity {

    protected abstract int getLayout();
    protected abstract String getToolbarTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        TextView toolbarText = (TextView) findViewById(R.id.toolbarTitle);
        toolbarText.setText(getToolbarTitle());

        Button btnAccount = (Button) findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                // cannot return to previous screens once home is pressed
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
