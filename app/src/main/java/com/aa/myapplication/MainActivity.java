package com.aa.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner quoteselector;
    Drawable back;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saved:
                Intent intent = new Intent(this, SavedQuotesActivity.class);
                startActivity(intent);
                return true;
            case R.id.info:
                Intent intent1 = new Intent(this, infoActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        quoteselector=(Spinner) findViewById(R.id.spinner);
        back=getResources().getDrawable(R.drawable.launcher_background);
        quoteselector.setPopupBackgroundDrawable(back);
    }

    public void generatorTrigger(View view) {
        Intent intent = new Intent(this, quoteActivity.class);
        int selectedUrl = quoteselector.getSelectedItemPosition();
        String[] urls =getResources().getStringArray(R.array.spinnervalues);
        String selected = urls[selectedUrl];
        intent.putExtra("selected", selected);
        startActivity(intent);
    }


}