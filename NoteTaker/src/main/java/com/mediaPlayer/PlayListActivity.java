package com.mediaPlayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.notetaker.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Kevin Damazyn
 * taken from androidhive.com
 * molded for note taker
 */
public class PlayListActivity extends ListActivity {
    //Song List
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

        SongsManager plm = new SongsManager();
        //get all the songs off the sdcard
        this.songsList = plm.getPlayList();

        //looping through playlist
        for (int i = 0; i < songsList.size(); i++) {
            //creating new HashMap
            HashMap<String, String> song = songsList.get(i);

            //add the songs
            songsListData.add(song);
        }

        //Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(this, songsListData, R.layout.playlist_item,
                new String[] { "songTitle"}, new int[] {R.id.songTitle});

        setListAdapter(adapter);

        //selecting single song
        ListView lv = getListView();
        //listening to single song when clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getting song index
                int songIndex = position;

                //Start the intent to listen to the song
                Intent in = new Intent(getApplicationContext(),
                        AndroidBuildingMusicPlayerActivity.class);
                //Sending the index to the player
                in.putExtra("songIndex", songIndex);
                setResult(100, in);
                //closing PlayListView
                finish();
            }
        });
    }
}
