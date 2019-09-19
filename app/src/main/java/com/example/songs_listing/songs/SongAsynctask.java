package com.example.songs_listing.songs;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ListView;

import com.example.songs_listing.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SongAsynctask extends AsyncTask<String, Void, List<Song>> {

    private Activity activity;
    private Context context;
    private SongsDatabase songsDatabase;
    private String title;
    private int row;

    public SongAsynctask(Context context, String title, Integer row) {
        this.context = context;
        this.activity = (Activity) context;
        this.songsDatabase = new SongsDatabase(context);
        this.title = title;
        this.row = row;
    }

    @Override
    protected List<Song> doInBackground(String... lists) {
        List<Song> songs = new ArrayList<>();
        try {
            URL url = new URL(lists[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type","application/json;charset=utf-8");
            connection.setRequestProperty("Accept", "*/*");
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
//            stringBuilder.append("{\"data\":[{\"Name\":\"Aaj Kal Zindagi\",\"Artist\":\"Shankar Mahadevan\",\"Album\":\"Wake Up Sid\"},{\"Name\":\"ALL GONE\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"California\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"CAN'T KEEP A GOOD MAN DOWN\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"Cedarwood Road\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"Closure\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"Death Whispered A Lullaby\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"Der Lagi Lekin\",\"Artist\":\"Shankar Mahadevan\",\"Album\":\"Zindagi Na Milegi Dobara\"},{\"Name\":\"DUST OFF YOUR BOOTS\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"Ending Credits\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"Every Breaking Wave\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"Geet\",\"Artist\":\"Amit Trivedi\",\"Album\":\"Udaan\"},{\"Name\":\"GET ENOUGH\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"Hope Leaves\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"ICEBERG MEADOWS\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"In My Time Of Need\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"Iris\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"LONG AND THE SHORT OF IT\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"Manja\",\"Artist\":\"Amit Trivedi\",\"Album\":\"Kai Po Che (2013)\"},{\"Name\":\"Maston Ka Jhund\",\"Artist\":\"Shankar Mahadevan\",\"Album\":\"Bhaag Milkha Bhaag (2013)\"},{\"Name\":\"NOTHING TO SHOW\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"Raised By Wolves\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"Sleep Like a Baby Tonight\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"Slow Motion Angreza\",\"Artist\":\"Shankar Mahadevan\",\"Album\":\"Bhaag Milkha Bhaag (2013)\"},{\"Name\":\"Song for Someone\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"STONES RIVER\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"The Miracle\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"The Troubles\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"This Is Where You Can Reach Me Now\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"To Rid The Disease\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"Volcano\",\"Artist\":\"U2\",\"Album\":\"Songs of Innocence\"},{\"Name\":\"WE'RE ALL GONNA DIE\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"Weakness\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"WHEN NO ONE IS AROUND\",\"Artist\":\"Crawdad Republic\",\"Album\":\"CRAWDAD REPUBLIC\"},{\"Name\":\"Windowpane\",\"Artist\":\"Opeth\",\"Album\":\"Damnation\"},{\"Name\":\"Zinda\",\"Artist\":\"Amit Trivedi\",\"Album\":\"Lootera (2013)\"},{\"Name\":\"Zinda\",\"Artist\":\"Shankar Mahadevan\",\"Album\":\"Bhaag Milkha Bhaag (2013)\"}]}");
            JSONObject object = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i=0;i<jsonArray.length();i++) {
                Song song = new Song();
                String name = jsonArray.getJSONObject(i).getString("Name");
                String artist = jsonArray.getJSONObject(i).getString("Artist");
                String album = jsonArray.getJSONObject(i).getString("Album");
                song.setName(name);
                song.setAlbum(album);
                song.setArtist(artist);
                long value = songsDatabase.createRecords(name, artist, album);
                Log.i(TAG, "doInBackground: " + value);
                songs.add(song);
            }
        }
        catch (Exception e) {
            Log.i("Error", "error");
            Log.i("Asynctask Error", "Couldn't fetch results!!" + e.getMessage());
        }
        return songs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(List<Song> songs) {
        ListView categoryList;
        SongsAdapter adapter = null;
        categoryList = activity.findViewById(R.id.category_list);
        if (songs == null || songs.size() == 0) {
            if (title.equals("Album")) {
                adapter = new SongsAdapter(context, R.layout.custom_category_layout, (ArrayList<SongList>) songsDatabase.selectRecordsByAlbum(), row);
            }
            else {
                adapter = new SongsAdapter(context, R.layout.custom_category_layout, (ArrayList<SongList>) songsDatabase.selectRecordsByArtist(), row);
            }
        }
        else {
            if (title.equals("Album")) {
                adapter = new SongsAdapter(context, R.layout.custom_category_layout, (ArrayList<SongList>) getAlbumSongMap(songs), row);
            }
            else {
                adapter = new SongsAdapter(context, R.layout.custom_category_layout, (ArrayList<SongList>) getArtistSongMap(songs), row);
            }
        }
        categoryList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onPostExecute(songs);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<SongList> getArtistSongMap(List<Song> songs) {
        Map<String, List<String>> songsMap = new LinkedHashMap<>();
        songsMap.clear();
        for (Song song : songs) {
            String artist = song.getArtist();
            if (songsMap.containsKey(artist)) {
                List<String> artistSongs = songsMap.get(artist);
                artistSongs.add(song.getName());
                songsMap.replace(artist, artistSongs);
            }
            else {
                List<String> artistSongs = new ArrayList<>();
                artistSongs.add(song.getName());
                songsMap.put(artist, artistSongs);
            }
        }
        List<SongList> songLists = new ArrayList<>();
        songLists.clear();
        for (Map.Entry<String, List<String>> artistSongs : songsMap.entrySet()) {
            songLists.add(new SongList(artistSongs.getKey(), artistSongs.getValue()));
        }
        return songLists;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<SongList> getAlbumSongMap(List<Song> songs) {
        Map<String, List<String>> songsMap = new LinkedHashMap<>();
        songsMap.clear();
        for (Song song : songs) {
            String album = song.getAlbum();
            if (songsMap.containsKey(album)) {
                List<String> albumSongs = songsMap.get(album);
                albumSongs.add(song.getName());
                songsMap.replace(album, albumSongs);
            }
            else {
                List<String> albumSongs = new ArrayList<>();
                albumSongs.add(song.getName());
                songsMap.put(album, albumSongs);
            }
        }
        List<SongList> songLists = new ArrayList<>();
        songLists.clear();
        for (Map.Entry<String, List<String>> albumSongs : songsMap.entrySet()) {
            songLists.add(new SongList(albumSongs.getKey(), albumSongs.getValue()));
        }
        return songLists;
    }

    @Override
    protected void onPreExecute() {
//        dialog = new ProgressDialog(context);
//        dialog.setMessage("Loading...Please Wait");
//        dialog.show();
        super.onPreExecute();
    }
}
