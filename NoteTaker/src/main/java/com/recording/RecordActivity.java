package com.recording;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.notetaker.R;

/**
 * Created by Dylan on 2/19/14.
 */
public class RecordActivity extends Activity {

    private Button recordButton;
    private TextView recordedText;
    private String filename;

    public void onCreate(Bundle SavedInstanceData) {
        super.onCreate(SavedInstanceData);
        setContentView(R.layout.record_activity);

        recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setText("Start new recording");

        recordedText = (TextView) findViewById(R.id.record_text);
    }

    public void startRecord(View view) {

        FileNameDialog fnd = new FileNameDialog();
        fnd.show(getFragmentManager(), "filename");

    }

    public void setFilename(String name) {
        filename = name;
        recordedText.setText(filename);
    }

}
