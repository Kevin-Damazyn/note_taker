package com.recording;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.speech.SpeechRecognizer;
import android.util.Log;

/**
 * Created by Dylan on 3/17/14.
 */
public class STTService extends Service {

    private MonitorReceiver monitor;

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        IntentFilter filter = new IntentFilter();
        filter.addAction("recordToggle");
        filter.addAction("resultReceived");
        monitor = new MonitorReceiver();
        registerReceiver(monitor, filter);

        return Service.START_STICKY;
    }

    public class MonitorReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "recordToggle") {
                Log.d("stt", "broadcast received");
            }
            else if (intent.getAction() == "resultReceived") {
                Log.d("stt", "result received");
                Intent i = new Intent();
                i.setAction("restartRecognizer");
                sendBroadcast(i);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
