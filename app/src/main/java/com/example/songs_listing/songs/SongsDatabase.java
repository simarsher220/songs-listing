package com.example.songs_listing.songs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SongsDatabase {

    private SongsHelper dbHelper;
    private SQLiteDatabase database;

    public final static String TABLE="songs"; // name of table
    public final static String NAME="name";  // name column
    public final static String ARTIST="artist"; // artist column
    public final static String ALBUM="album";  // album column

    /**
     *
     * @param context
     */
    public SongsDatabase(Context context){
        dbHelper = new SongsHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(String name, String artist, String album){
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(ARTIST, artist);
        values.put(ALBUM, album);
        Log.i(TAG, "createRecords: inserting values into table " + name + " " + artist + " " + album);
        long value =  database.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Cursor cur = database.rawQuery("SELECT COUNT(*) FROM songs", null);
        if (cur != null) {
            cur.moveToFirst();                       // Always one row returned.
            Log.i(TAG, "createRecords: total records in the table is " + cur.getInt (0));
        }
        return value;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<SongList> selectRecordsByArtist() {
        String[] columns = new String[] {NAME, ARTIST, ALBUM};
        Map<String, List<String>> songs = new LinkedHashMap<>();
        songs.clear();
        Cursor mCursor = database.rawQuery("select " + NAME + ", " + ARTIST  + " from " + TABLE, null);
        mCursor.moveToFirst();
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    String album = mCursor.getString(mCursor.getColumnIndex(ARTIST));
                    Log.i(TAG, "selectRecordsByArtist: " + mCursor.getString(mCursor.getColumnIndex(ARTIST)) + " - " + mCursor.getString(mCursor.getColumnIndex(NAME)));
                    if (songs.containsKey(album)) {
                        List<String> albumSongs = songs.get(album);
                        albumSongs.add(mCursor.getString(mCursor.getColumnIndex(NAME)));
                        songs.replace(album, albumSongs);
                    }
                    else {
                        List<String> albumSongs = new ArrayList<>();
                        albumSongs.add(mCursor.getString(mCursor.getColumnIndex(NAME)));
                        songs.put(album, albumSongs);
                    }
                }while (mCursor.moveToNext());
            }
        }
        List<SongList> songLists = new ArrayList<>();
        for (Map.Entry<String, List<String>> artistSongs : songs.entrySet()) {
            songLists.add(new SongList(artistSongs.getKey(), artistSongs.getValue()));
        }
        return songLists;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<SongList> selectRecordsByAlbum() {
        String[] columns = new String[] {NAME, ARTIST, ALBUM};
        Map<String, List<String>> songs = new LinkedHashMap<>();
        Cursor mCursor = database.rawQuery("select " + NAME + ", " + ALBUM  + " from " + TABLE, null);
        int t = 0;
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    t++;
                    Log.i(TAG, "" + t);
                    String album = mCursor.getString(mCursor.getColumnIndex(ALBUM));
                    if (songs.containsKey(album)) {
                        List<String> albumSongs = songs.get(album);
                        albumSongs.add(mCursor.getString(mCursor.getColumnIndex(NAME)));
                        songs.replace(album, albumSongs);
                    }
                    else {
                        List<String> albumSongs = new ArrayList<>();
                        albumSongs.add(mCursor.getString(mCursor.getColumnIndex(NAME)));
                        songs.put(album, albumSongs);
                    }
                }while (mCursor.moveToNext());
            }
        }
        List<SongList> songLists = new ArrayList<>();
        for (Map.Entry<String, List<String>> artistSongs : songs.entrySet()) {
            songLists.add(new SongList(artistSongs.getKey(), artistSongs.getValue()));
        }
        return songLists;
    }
}
