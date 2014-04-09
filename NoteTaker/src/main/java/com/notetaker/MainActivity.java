package com.notetaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.filebrowser.FileChooser;
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
        Intent i = new Intent(this, FileChooser.class);
        startActivity(i);
    }

}
