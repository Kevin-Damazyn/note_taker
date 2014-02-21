package com.notetaker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.recording.RecordActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.OnClickListener;


public class MainActivity extends Activity {

    public static final int SETTINGS_REQUEST_CODE = 2301;

    private TextView username;

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main);

        username = (TextView) findViewById(R.id.title_name);

        updatePrefs();
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

    }

    public void newRecord(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
    }

}
