package com.mediaPlayer;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Kevin Damazyn
 * taken from androidhive.com
 * molded for note taker
 */
public class SongsManager {

    //card path for media (.mp3s)
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String> >();

    //constructor
    public SongsManager(){

    }

    public ArrayList<HashMap<String, String>> getPlayList(){
        File home = new File("storage/extSdCard/Music/The 1975/The 1975");


        FilenameFilter extensionFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return (name.endsWith(".mp3") || name.endsWith(".MP3"));
            }
        };

        File[] songs = home.listFiles(extensionFilter);

        if (songs.length > 0) {
            for (File file : songs) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());

                //add a song to the list
                songsList.add(song);
            }
        }
        //return the list of songs
        return songsList;
    }
}
