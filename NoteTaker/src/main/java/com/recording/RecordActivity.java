package com.recording;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.notetaker.R;

/**
 * Created by Dylan on 2/19/14.
 */
public class RecordActivity extends Activity {

    private Button recordButton;

    public void onCreate(Bundle SavedInstanceData) {
        super.onCreate(SavedInstanceData);
        setContentView(R.layout.record_activity);

        recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setText("Start new recording");
    }

    public void startRecord(View view) {

        FileNameDialog fnd = new FileNameDialog();
        fnd.show(getFragmentManager(), "filename");

    }

}
