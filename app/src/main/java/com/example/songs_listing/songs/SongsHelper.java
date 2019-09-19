package com.example.songs_listing.songs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class SongsHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DBName";
    private static final int DATABASE_VERSION = 3;
    private Context  context;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table IF NOT EXISTS songs(name text primary key not null, artist text not null, album text not null);";

    public SongsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(TAG, "onCreate: creating table songs");
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(SongsHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS songs");
        onCreate(database);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i(TAG, "onOpen: dropping table songs");
//        db.execSQL("DROP TABLE IF EXISTS songs");
        onCreate(db);
    }
}
