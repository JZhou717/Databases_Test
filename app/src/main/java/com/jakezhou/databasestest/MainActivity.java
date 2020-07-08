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
        //Note INT PRIMARY KEY != INTEGER PRIMARY KEY. Former is typeless (like any other column) while the latter must be 64 bit signed integer that is an alias for the ROWID. INTEGER PRIMARY KEY will never be null
        eventsDB.execSQL("CREATE TABLE IF NOT EXISTS events (event VARCHAR, year INT(4), id INTEGER PRIMARY KEY)");
        //Deleting db table - IF EXISTS is optional
        //eventsDB.execSQL("DROP TABLE IF EXISTS events");



        //Altering data in DB
        eventsDB.execSQL("DELETE FROM events WHERE event LIKE 'Fall of %'");
        eventsDB.execSQL("INSERT INTO events (event, year) VALUES ('Fall of Western Roman Empire', 476)");
        eventsDB.execSQL("INSERT INTO events (event, year) VALUES ('Fall of Eastern Roman Empire', 1453)");
        eventsDB.execSQL("DELETE FROM events WHERE event LIKE 'Start of %'");
        eventsDB.execSQL("INSERT INTO events (event, year) VALUES ('Start of the French Revolution', 1)");
        eventsDB.execSQL("UPDATE events SET year = 1789 WHERE event = 'Start of the French Revolution'");
        eventsDB.execSQL("DELETE FROM events WHERE event LIKE 'Columbus %'");
        eventsDB.execSQL("INSERT INTO events (event, year) VALUES ('Columbus arrives at New World', 1492)");


        //Navigating DB
        //Examples of different, simple SQL queries
        Cursor c = eventsDB.rawQuery("SELECT * FROM events", null);
        //Cursor c = eventsDB.rawQuery("SELECT * FROM events WHERE year = 476", null);
        //Cursor c = eventsDB.rawQuery("SELECT * FROM events WHERE event LIKE 'Fall of %'", null);
        //Cursor c = eventsDB.rawQuery("SELECT * FROM events WHERE year = 1453 LIMIT 1", null);

        int columbus_id = -1;

        //Iterating through the db the first time
        int eventIndex = c.getColumnIndex("event");
        int yearIndex = c.getColumnIndex("year");
        int idIndex = c.getColumnIndex("id");
        c.moveToFirst();
        Log.i("New DB", "////////START OF QUERY////////");
        while(!c.isAfterLast()) {
            Log.i("Event", c.getString(eventIndex));
            Log.i("Year", Integer.toString(c.getInt(yearIndex)));
            Log.i("ID", Integer.toString(c.getInt(idIndex)));

            if(c.getString(eventIndex).equals("Columbus arrives at New World"))
                columbus_id = c.getInt(idIndex);

            c.moveToNext();
        }

        //Deleting columbus event - Either solution works
        //eventsDB.delete("events", "id = " + columbus_id, null);
        eventsDB.execSQL("DELETE FROM events WHERE id = " + columbus_id);

        //Iterating through the db after update
        //Have to do another rawQuery to get updated results
        c = eventsDB.rawQuery("SELECT * FROM events", null);
        c.moveToFirst();
        Log.i("New DB", "////////START OF QUERY////////");
        while(!c.isAfterLast()) {
            Log.i("Event", c.getString(eventIndex));
            Log.i("Year", Integer.toString(c.getInt(yearIndex)));
            Log.i("ID", Integer.toString(c.getInt(idIndex)));

            c.moveToNext();
        }
    }
}
