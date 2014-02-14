package com.notetaker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Dylan on 2/14/14.
 */
public class SettingsActivity extends Activity {

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onStart() {
        Log.d("gui","settings started");
        super.onStart();
    }

    public void onPause() {
        Log.d("gui", "settings paused");
        super.onPause();
        //TODO Add result remove.
    }

    public void onStop() {
        Log.d("gui", "settings stopped");
        super.onStop();
    }

}
