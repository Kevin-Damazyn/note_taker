package com.notetaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.filebrowser.FileChooser;
import com.recording.RecordActivity;
import com.recording.STTActivity;
import com.transcription.SphinxTranscriptionService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends Activity {

    public static final int SETTINGS_REQUEST_CODE = 2301;

    private TextView username;
    static private File direct;

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onStart() {
        super.onStart();

        Log.d("gui", "Start is called in main.");

        EditText usernamein = (EditText) findViewById(R.id.user_name_field);

        if (usernamein != null) {
            Log.d("gui", "usernamein exists");
            if (usernamein.getText() != null) {
                Log.d("gui", "name exists");
                username.setText(usernamein.getText());
            }
        }
    }

    public void toSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,SETTINGS_REQUEST_CODE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("gui","option menu created");
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("gui","option item selected");
        if (item.getItemId() == R.id.action_settings) {
            Log.d("gui","setting selected");

            Intent i = new Intent(this, SettingsActivity.class);

            startActivityForResult(i, 111);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        updatePrefs();
    }

    private void updatePrefs() {

        SharedPreferences mySharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (mySharedPrefs.getString("username","None") != "None")
            username.setText("Welcome back " + mySharedPrefs.getString("username","None"));

        if (mySharedPrefs.getBoolean("AltTheme", true)){
            setContentView(R.layout.activity_main_dark);
        } else {
            setContentView(R.layout.activity_main);
        }

    }

    public void gotoRecord(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    public void browseRecordings(View view) {
        Intent i = new Intent(this, FileChooser.class);
        startActivity(i);
    }

    public void processRecording(View view) {
        Intent i = new Intent(this, SphinxTranscriptionService.class);
        startService(i);
    }


}
