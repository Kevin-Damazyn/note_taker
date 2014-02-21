package com.recording;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.notetaker.CustomPreferences;
import com.notetaker.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by Dylan on 2/19/14.
 */
public class RecordActivity extends Activity {

    private Button recordButton;
    private TextView recordedText;
    private String filename;
    private boolean recording = false;
    private MediaRecorder mr;
    private MediaPlayer mp;
    private File direct;

    private static String stringParser(String input) {
        String output = "";
        String pattern = ".,? ";

        Log.d("parser", "Beginning parse loop");
        for (Character c : input.toCharArray()) {
            Log.d("parser", "Testing char:" + c);
            if (pattern.contains(c.toString())) {
                output += "_";
            }
            else {
                output += c;
            }
        }

        char[] temp = output.toCharArray();
        String newOut = "";
        for (int i = 0; i<temp.length; i++) {
            if (temp[i] == '_' && i<temp.length-1) {
                if (temp[i+1] != '_')
                    newOut+='_';
            }
            else
                newOut+=temp[i];
        }
        char[] temptwo = newOut.toCharArray();
        if (temptwo[temptwo.length-1] == '_')
            temptwo[temptwo.length-1] = '\0';
        newOut = "";
        for (Character c : temptwo)
            newOut+=c;
        return newOut;

    }

    public void onCreate(Bundle SavedInstanceData) {
        super.onCreate(SavedInstanceData);
        setContentView(R.layout.record_activity);

        recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setText("Start new recording");

        recordedText = (TextView) findViewById(R.id.record_text);

        mp = new MediaPlayer();
        mr = new MediaRecorder();

        direct = new File(getDir(Environment.DIRECTORY_ALARMS, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "recordings");
        direct.mkdir();
    }

    public void startRecord(View view) {
        if (!recording) {
            try {
                mp.stop();
            } catch(IllegalStateException e) { }
            FileNameDialog fnd = new FileNameDialog();
            fnd.show(getFragmentManager(), "filename");
            recording = !recording;
        }
        else {
            mr.stop();
            mr.reset();
            try {
                mp.setDataSource(direct.getAbsolutePath() + "/test.3gp");
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recording = !recording;
        }
    }

    public void startRecording() {
        recordButton.setText("Stop recording");

        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mr.setOutputFile(direct.getAbsolutePath()+ "/test.3gp");

        try {
            mr.prepare();
        } catch (IOException e) {
            Log.e("record","prepare failed on recorder");
        }

        mr.start();
    }

    public void setFilename(String name) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        filename = "/" + name + "_" + prefs.getString("username", "None") + ".3gp";
        recordedText.setText(filename);
    }

    public void onDestroy() {
        for (File f : direct.listFiles())
            Log.d("record",f.getName());
        super.onDestroy();
    }
}
