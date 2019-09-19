package com.example.songs_listing.songs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.songs_listing.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SongsAdapter extends ArrayAdapter<SongList> {

    ArrayList<SongList> songLists;
    Context context;
    int resource, rowCount;

    public SongsAdapter(Context context, int resource, ArrayList<SongList> songLists, int rowCount) {
        super(context, resource, songLists);
        this.songLists = songLists;
        this.context = context;
        this.resource = resource;
        this.rowCount = rowCount;
    }

    private static class ViewHolder {

        private TextView titleView;
        private GridLayout gridLayout;

        private ViewHolder (View v) {
            this.titleView = v.findViewById(R.id.title);
            this.gridLayout = v.findViewById(R.id.songs);
        }
    }

    @Override
    public int getCount() {
        return songLists.size();
    }

    @Override
    public SongList getItem(int position) {
        return songLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null, true);
            Log.i(TAG, "getView: convert view is " + convertView);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            SongList songList = getItem(position);
            holder.gridLayout.removeAllViews();
            int row = rowCount;
            int total = songList.getSongs().size();
            int maxLength = 0;
            for (int i=0;i<total;i++) {
                maxLength = maxLength > songList.getSongs().get(i).length() ? maxLength : songList.getSongs().get(i).length();
            }
            Log.i(TAG, "getView: listing all songs" + songList.getSongs());
            int column = total / row;
            holder.gridLayout.setColumnCount(column == 0 ? column + 1 : column);
            holder.gridLayout.setRowCount(row);
            int i = 0;
            for(int c = 0;c < column;c++){
                for (int r=0;r < row;r++) {
                    TextView button = new TextView(convertView.getContext());
                    button.setBackgroundColor(Color.WHITE);
                    button.setText(songList.getSongs().get(i++));
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    button.setLayoutParams(lp);
                    holder.gridLayout.setUseDefaultMargins(true);
                    GridLayout.Spec rowSpan = GridLayout.spec(r);
                    GridLayout.Spec colspan = GridLayout.spec(c);
                    GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                            rowSpan, colspan);
                    holder.gridLayout.addView(button, gridParam);
                }
            }
//            for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
//                if (c == row) {
//                    c = 0;
//                    r++;
//                }
//                TextView button = new TextView(convertView.getContext());
//                button.setBackgroundColor(Color.WHITE);
//                button.setText(songList.getSongs().get(i));
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(100, 30);
//                button.setLayoutParams(lp);
//                holder.gridLayout.setUseDefaultMargins(true);
//                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
//                GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
//                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
//                        rowSpan, colspan);
//                holder.gridLayout.addView(button, gridParam);
//            }
            holder.titleView.setText(songList.getProperty());
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
