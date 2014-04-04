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
import com.recording.STTActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.OnClickListener;


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

}
