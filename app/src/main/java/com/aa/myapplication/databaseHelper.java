package com.aa.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aa.myapplication.models.Quote;

import java.util.ArrayList;
import java.util.List;

public class databaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quotes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "quotes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_AUTHOR = "author";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_QUOTES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_AUTHOR + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_QUOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addOne(Quote quote){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CONTENT, quote.getContent());
        cv.put(COLUMN_AUTHOR, quote.getAuthor());

        long insert = db.insert(TABLE_NAME,null,cv);
        if(insert==-1){
            return false;
        }else {
            return true;
        }
    }
    public boolean deleteOne(Quote quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_CONTENT + " = ?";
        String[] whereArgs = { quote.getContent() };
        int rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs);
        return rowsDeleted > 0;
    }
    public List<Quote> getAll(){
        List<Quote> savedQuotes = new ArrayList<Quote>();
        String querryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querryString,null);
        if(cursor.moveToFirst()){
            //loop through the cursor
            do{
                int columnID = cursor.getInt(0);
                String content = cursor.getString(1);
                String author = cursor.getString(2);

                Quote newQuote = new Quote(content,author);
                savedQuotes.add(newQuote);
            }while (cursor.moveToNext());
        }else{
            //this is where u handle fails if neccesary

        }
        //always fucking close databases
        cursor.close();
        db.close();
        return savedQuotes;
    }
}
