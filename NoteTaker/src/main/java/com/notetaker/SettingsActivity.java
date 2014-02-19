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

        getFragmentManager().beginTransaction().replace(android.R.id.content, new CustomPreferences()).commit();

    }

    public void onStart() {
        Log.d("gui","settings started");
        super.onStart();
    }

    public void onPause() {
        Log.d("gui", "settings paused");
        super.onPause();
    }

    public void onStop() {
        Log.d("gui", "settings stopped");
        super.onStop();
    }

}
