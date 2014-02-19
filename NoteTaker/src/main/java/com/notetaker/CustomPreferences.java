package com.notetaker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by Dylan on 2/18/14.
 */
public class CustomPreferences extends PreferenceFragment {

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
