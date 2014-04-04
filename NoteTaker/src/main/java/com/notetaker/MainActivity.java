package com.notetaker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mediaPlayer.AndroidBuildingMusicPlayerActivity;
import com.recording.RecordActivity;
import com.recording.STTActivity;


public class MainActivity extends Activity {

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStart() {
        super.onStart();

        Log.d("gui", "Start is called in main.");
    }

    public void gotoRecord(View view) {
        Intent intent = new Intent(this, STTActivity.class);
        startActivity(intent);
    }

    public void browseRecordings(View view) {
        Intent i = new Intent(this, AndroidBuildingMusicPlayerActivity.class);
        startActivity(i);
    }

}
