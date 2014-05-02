package com.recording;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.notetaker.MainActivity;
import com.notetaker.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import simplesound.pcm.PcmAudioHelper;
import simplesound.pcm.WavAudioFormat;

/**
 * Created by Kevin Damazyn on 5/1/14.
 */
public class RecordActivity extends Activity {

    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = MainActivity.getDirect().getAbsolutePath();
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 16000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean recording = false;
    private Button recordButton;
    String finalFilename;

    public void onCreate(Bundle SavedInstanceData) {
        super.onCreate(SavedInstanceData);
        setContentView(R.layout.record_activity);

        recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setText("Start recording");
        recordButton.setOnClickListener(recordBtnClick);

        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);
    }

    private String getTempFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()) {
            file.mkdirs();
        }

        File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);
        if(tempFile.exists()) {
            tempFile.delete();
        }

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + getFinalFilename() + AUDIO_RECORDER_FILE_EXT_WAV);
    }

    private void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,bufferSize);

        recorder.startRecording();
        recording = true;

        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeAudioDataToFile();
            }
        }, "RecordActivity Thread");

        recordingThread.start();
    }

    private void writeAudioDataToFile() {
        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int read = 0;

        if (os != null) {
            while(recording) {
                read = recorder.read(data,0,bufferSize);

                if(AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                convertToWav(new File(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertToWav(File rawFile) throws IOException {
        File wavFile = new File(getFilename());
        int channelForMono = 1;
        WavAudioFormat.Builder audioFormat = new WavAudioFormat.Builder();
        audioFormat.channels(channelForMono);
        audioFormat.sampleRate(RECORDER_SAMPLERATE);
        audioFormat.sampleSizeInBits(RECORDER_BPP);

        try {
            PcmAudioHelper.convertRawToWav(audioFormat.build(),rawFile,wavFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopRecording() {
        if(recorder != null) {
            recording = false;

            recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

    }


    public void setFilename(String name) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        finalFilename = "/" + name + "_" + prefs.getString("username", "None");
    }

    public String getFinalFilename() {
        return finalFilename;
    }

    private View.OnClickListener recordBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (!recording) {
                FileNameDialog fnd = new FileNameDialog();
                fnd.show(getFragmentManager(), "filename");
                recordButton.setText("Stop recording");
                recording = true;
                startRecording();
            } else {
                recordButton.setText("Start recording");
                recording = false;
                stopRecording();
            }
        }
    };

}
