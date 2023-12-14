package com.aa.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.PrecomputedTextCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aa.myapplication.models.Quote;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SavedQuotesActivity extends AppCompatActivity {
    ListView lv;
    databaseHelper db;
    int trashClickCount;
    MenuItem trash;
    ArrayAdapter quoteAdapter;
    TextView snackTarget;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savedquotesmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoo:
                Intent intent1 = new Intent(this, infoActivity.class);
                startActivity(intent1);
                return true;
            case R.id.prev:
                snackTarget.setText("");
                return true;
            case R.id.trash:
                trash=item;
                trashClickCount++;
                if(trashClickCount%2==1){
                    snackTarget.setText("Click the quotes you want to delete;" + "\n" + "Click the trashcan again to stop deleting;");
                    trash.setIcon(R.drawable.activedeletemodenoback);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            Quote clickedQuote = (Quote) adapterView.getItemAtPosition(position);
                            db.deleteOne(clickedQuote);
                            quoteAdapter.remove(clickedQuote);
                            quoteAdapter.notifyDataSetChanged();
                        }
                    });
                }else if(trashClickCount%2==0){
                    snackTarget.setText("");
                    trash.setIcon(R.drawable.trashnoback);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Quote clickedQuote = (Quote) adapterView.getItemAtPosition(i);
                            String authorString = clickedQuote.getAuthor();
                            String contentString = clickedQuote.getContent();
                            snackTarget.setText(contentString + "\n" + authorString);
                        }
                    });
                }
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_quotes);
        db= new databaseHelper(SavedQuotesActivity.this);
        lv = findViewById(R.id.lv);
        List<Quote> savedQuotes = db.getAll();
        trashClickCount=0;
        snackTarget=(TextView)findViewById(R.id.snacktarget);
        quoteAdapter = new ArrayAdapter<Quote>(SavedQuotesActivity.this, android.R.layout.simple_selectable_list_item, savedQuotes);
        lv.setAdapter(quoteAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quote clickedQuote = (Quote) adapterView.getItemAtPosition(i);
                String authorString = clickedQuote.getAuthor();
                String contentString = clickedQuote.getContent();
                snackTarget.setText(contentString + "\n" + authorString);
            }
        });



    }

}
