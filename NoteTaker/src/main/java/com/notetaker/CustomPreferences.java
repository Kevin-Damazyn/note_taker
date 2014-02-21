package com.notetaker;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by Dylan on 2/18/14.
 */
public class CustomPreferences extends PreferenceFragment {

    public static final int[] formatList = { MediaRecorder.OutputFormat.AAC_ADTS, MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
