package com.jakezhou.databasestest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase eventsDB = this.openOrCreateDatabase("Events", Context.MODE_PRIVATE, null);
        //Creating db table
        eventsDB.execSQL("CREATE TABLE IF NOT EXISTS events (event VARCHAR, year INT(4))");

        //Adding data to DB
        eventsDB.execSQL("INSERT INTO events (event, year) VALUES ('Fall of Western Roman Empire', 476)");
        eventsDB.execSQL("INSERT INTO events (event, year) VALUES ('Fall of Eastern Roman Empire', 1453)");

        //Navigating DB
        Cursor c = eventsDB.rawQuery("SELECT * FROM events", null);

        int eventIndex = c.getColumnIndex("event");
        int yearIndex = c.getColumnIndex("year");

        c.moveToFirst();

        while(!c.isAfterLast()) {
            Log.i("Event", c.getString(eventIndex));
            Log.i("Year", Integer.toString(c.getInt(yearIndex)));

            c.moveToNext();
        }
    }
}
