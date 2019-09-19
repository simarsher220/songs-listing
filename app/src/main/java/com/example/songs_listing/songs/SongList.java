package com.example.songs_listing.songs;

import java.util.List;

public class SongList {

    private String property;
    private List<String> songs;

    public SongList(String property, List<String> songs) {
        this.property = property;
        this.songs = songs;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

}
