package com.aa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class infoActivity extends AppCompatActivity {
    TextView creds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        creds=(TextView) findViewById(R.id.creditTrigger);
    }

    public void credits(View view) {
        Snackbar credits = Snackbar.make(creds, "Made By: A.S Android development Studio", Snackbar.LENGTH_LONG );
        credits.show();
    }

}