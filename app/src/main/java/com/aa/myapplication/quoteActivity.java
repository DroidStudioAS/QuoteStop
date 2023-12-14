package com.aa.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aa.myapplication.*;
import com.aa.myapplication.models.Quote;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class quoteActivity extends AppCompatActivity {
    OkHttpClient client;
    TextView authorView;
    TextView contentView;
    ImageView trigger;
    ImageView addButton;
    String geturl;
    Quote prevQuote;
    ArrayList<Quote> quotes;
    int clickCount;
    int quoteIndex;
    databaseHelper dbHelper;
    Drawable afterHeart;
    static Quote quote;
    Drawable beforeHeart;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        Intent incoming = getIntent();

        geturl = incoming.getStringExtra("selected");
        client=new OkHttpClient();
        addButton=(ImageView)findViewById(R.id.addButton);
        dbHelper=new databaseHelper(this);
        authorView=(TextView) findViewById(R.id.authorView);
        contentView=(TextView) findViewById(R.id.contentView);
        trigger=(ImageView) findViewById(R.id.trigger);
        clickCount=0;
        prevQuote=null;
        afterHeart=getResources().getDrawable(R.drawable.afterheartnobackground);
        beforeHeart=getResources().getDrawable(R.drawable.beforeheartnobackground);
        quotes=new ArrayList<Quote>();
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get();
                changeDrawable(beforeHeart);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper dbHelper = new databaseHelper(quoteActivity.this);
                if(!contentView.getText().toString().isEmpty()){
                    Quote toSave = new Quote(contentView.getText().toString(),authorView.getText().toString());
                    boolean succes = dbHelper.addOne(toSave);
                        if(succes){
                            changeDrawable(afterHeart);
                        }
                }else  {
                    Toast toast = Toast.makeText(quoteActivity.this, "There is no Quote to save!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void changeDrawable(Drawable drawable) {
        addButton.setImageDrawable(drawable);
    }


    public void get(){
        Request request =  new Request.Builder().url(geturl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try{

                    Log.i("firstSize", String.valueOf(quotes.size()));
                    String jsonResp = response.body().string();
                    Gson gson = new Gson();
                    quote =gson.fromJson(jsonResp, Quote.class);
                    contentView.setText(quote.getContent());
                    authorView.setText(quote.getAuthor());
                    quotes.add(quote);
                    if(quotes.size()==1){
                        scheduleNotification(getApplicationContext(), 5000, quote.getAuthor(), quote.getContent() );
                    }
                    quoteIndex = quotes.indexOf(quote);
                    if(quoteIndex!=0){
                        prevQuote=quotes.get(quoteIndex-1);
                        Log.i("prevQuote", prevQuote.getContent());
                    }
                    Log.i("qupteindex", String.valueOf(quoteIndex));
                    clickCount++;

                }catch (IOException exc){
                    exc.printStackTrace();
                }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.prev:
                if(quoteIndex!=0){
                    contentView.setText(prevQuote.getContent());
                    authorView.setText(prevQuote.getAuthor());
                }
                return true;
            case R.id.saved:
                Intent intent = new Intent(this, SavedQuotesActivity.class);
                startActivity(intent);
                return true;
            case R.id.info:
                Intent infoIntent = new Intent(this, infoActivity.class);
                startActivity(infoIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        quotes.clear();

    }
    public static void scheduleNotification(Context context, long time, String title, String text){
        Intent intent = new Intent(context, notificationReciver.class);
        intent.putExtra("title", title );
        intent.putExtra("content",text);
        PendingIntent intent1 = PendingIntent.getBroadcast(context,42,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, intent1);
    }
}