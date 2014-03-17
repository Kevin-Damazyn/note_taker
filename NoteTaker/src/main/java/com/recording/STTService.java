package com.recording;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.SpeechRecognizer;
import android.util.Log;

/**
 * Created by Dylan on 3/17/14.
 */
public class STTService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
