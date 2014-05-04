package com.mediaPlayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.notetaker.MainActivity;
import com.notetaker.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static android.media.MediaPlayer.*;

/**
 * Kevin Damazyn
 * taken from androidhive.com
 * molded for note taker
 */
public class AndroidBuildingMusicPlayerActivity extends Activity
        implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private TextView textFromFile;
    //Media Player
    private MediaPlayer mp;
    //handler to update UI timer, progress bar etc
    private Handler mHandler = new Handler();
    private Utilities utils;
    private int seekForwardTime = 5000; //in milliseconds
    private int seekBackwardTime = 5000; //in milliseconds
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private String song;
    private String songTitle;
//    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        // All player buttons
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        ImageButton btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        textFromFile = (TextView) findViewById(R.id.text_from_recording);

        FilenameFilter extensionFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return (name.endsWith(".wav") || name.endsWith(".WAV"));
            }
        };

       File[] files = MainActivity.getDirect().listFiles(extensionFilter);

        Intent intent = getIntent();

        if (files != null) {
            Log.d("stt", "found some files");
            try
            {
                String name = intent.getStringExtra("GetFileName").replaceAll(".wav", ".txt");
                InputStream instream = openFileInput(name);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line, line1 = "";

                    try
                    {
                        while ((line = buffreader.readLine()) != null)
                            line1+=line;
                        Log.d("stt", "line is as follows: ");
                        Log.d("stt", line1);
                        textFromFile.setText(line1);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                String error="";
                error=e.getMessage();
            }
        }

        song = intent.getStringExtra("GetFilePath");
        songTitle = intent.getStringExtra("GetFileName");

        //mediaplayer
        mp = new MediaPlayer();
        utils = new Utilities();

        //Listeners
        songProgressBar.setOnSeekBarChangeListener(this); //Important
        mp.setOnCompletionListener(this); // Important

        //play button click event
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check already playing
                if(mp.isPlaying()) {
                    if(mp != null) {
                        mp.pause();
                        //changing button image to play
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                }else {
                    //Resume song
                    if(mp != null) {
                        mp.start();
                        //changing button image to pause
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }
            }
        });

        //Forward button, forwards song to specified seconds
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        //Backward button, backward song to specified seconds
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        //Next button, plays next song in the list
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                playSong(song);//get songPath from filechooser);

            }
        });

        //Back button, plays previous song in the list
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                playSong(song); //get songPath from filechooser

            }
        });

        //Repeat button, Enables repeat button
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        //Shuffle button, enables shuffle button
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }else{
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });

    }

    //Play the song method
    public void playSong(String songPath){
        //Play song
        try {
            mp.reset();
            mp.setDataSource(songPath);
            mp.prepare();
            mp.start();
            //Display song title
            songTitleLabel.setText(songTitle);

            //Changing Play button to Pause
            btnPlay.setImageResource(R.drawable.btn_pause);

            //set progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            //update progress bar
            updateProgressBar();

        } catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        //Update timer on seekbar
        public void updateProgressBar() {
            mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    //background runnable thread
    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            try {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            //displaying Total Duration Time
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            //Displaying time completed playing
            songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            //Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            songProgressBar.setProgress(progress);

            //running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
            } catch (IllegalStateException e) {

            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    //when user starts moving the progress handler
    @Override
    public void onStartTrackingTouch(SeekBar seekBar){
        //remove message handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    //when user stops moving the progress handler
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        //forward or backward to certain seconds
        mp.seekTo(currentPosition);

        //update timer progress again
        updateProgressBar();
    }

    //On song playing completed check for repeat/shuffle
    @Override
    public void onCompletion(MediaPlayer arg0) {
                //play first song
                playSong(song);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
    }
}

