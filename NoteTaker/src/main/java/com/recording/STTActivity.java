package com.recording;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.notetaker.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Dylan on 3/17/14.
 */
public class STTActivity extends Activity {

    private boolean recording = false;
    private boolean recorder_alive = false;
    private SpeechRecognizer recognizer;
    private Intent speech_intent;
    private Intent monitor_intent;
    private RecognizerReceiver receiver;
    private String partial = "";
    private String filename = "";

    private TextView output;

    private MediaRecorder mRecorder;
    private File directory;

    public class RecognizerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "restartRecognizer") {
                Log.d("stt", "Restarting recognizer...");
                recognizer.startListening(speech_intent);
            }
        }
    }

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);

        setContentView(R.layout.stt_layout);
        output = (TextView) findViewById(R.id.recorded_text);

        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(listener);

        speech_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speech_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speech_intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        directory = getDir("recordings", Context.MODE_PRIVATE);
        directory.mkdir();

        mRecorder = new MediaRecorder();

        //TODO Create monitor
        monitor_intent = new Intent(this, STTService.class);
        startService(monitor_intent);

        IntentFilter filter = new IntentFilter();
        filter.addAction("restartRecognizer");
        receiver = new RecognizerReceiver();
        registerReceiver(receiver, filter);
    }

    public void RecordToggle(View view) {
        Button b = (Button) view;
        if (!recording) {
            b.setText("Stop Recording");

            Intent i = new Intent();
            i.setAction("recordToggle");
            sendBroadcast(i);

            recognizer.startListening(speech_intent);

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(directory.getAbsolutePath() + "/temp.3gp");

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.d("stt", "prepare failed");
                e.printStackTrace();
            }

            //mRecorder.start();
            recording = !recording;
        }
        else {
            b.setText("Start Recording");

            recognizer.stopListening();
            //mRecorder.stop();
            stopService(monitor_intent);

            //-- Alert for file name
            Log.d("stt", "creating alert");
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Enter file name");
            alert.setMessage("Hitting cancel will throw away files");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    filename = input.getText().toString();
                    try {
                        handleFiles();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    filename = "";
                }
            });

            alert.show();

            recording = !recording;
        }
    }

    RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
            Log.e("stt", "error in recognizer" + i);
            output.setText(output.getText() + " " + Html.fromHtml(partial));

            if (recording) {
                Intent it = new Intent();
                it.setAction("resultReceived");
                sendBroadcast(it);
            }
        }

        @Override
        public void onResults(Bundle bundle) {
            if (bundle != null) {

                ArrayList<String> out = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                output.setText(output.getText() + " " + out.get(0));

                if (recording) {
                    Intent it = new Intent();
                    it.setAction("resultReceived");
                    sendBroadcast(it);
                }
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            if (bundle != null) {
                ArrayList<String> out = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                partial= "<" + out.get(0) + ">";
            }
        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    public void onStop() {
        super.onStop();
        //TODO Stop service
        stopService(monitor_intent);
    }

    private void handleFiles() throws IOException {
        Log.d("stt", filename);

        String out = output.getText().toString();

        File f = getFileStreamPath(filename + ".txt");
        f.createNewFile();

        FileOutputStream fos = openFileOutput(f.getName(), Context.MODE_PRIVATE);

        fos.write(out.getBytes());
        fos.flush();
        fos.close();

        for (File fi : getFilesDir().listFiles())
            Log.d("stt", fi.getName());
    }
}
