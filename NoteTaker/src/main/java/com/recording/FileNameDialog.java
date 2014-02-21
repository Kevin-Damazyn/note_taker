package com.recording;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.notetaker.R;

/**
 * Created by Dylan on 2/19/14.
 */
public class FileNameDialog extends DialogFragment {

    private RecordActivity parent;
    private EditText et;

    public Dialog onCreateDialog(Bundle SavedInstanceState) {

        parent = (RecordActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater lf = getActivity().getLayoutInflater();
        builder.setView(lf.inflate(R.layout.dialog_filename, null));


        //TODO Needs to be tested and debugged; additionally, cancelling needs to be fleshed out.
        builder.setTitle("Select File")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et = (EditText) getDialog().findViewById(R.id.filename_entry);

                        String temp = et.getText().toString();
                        parent.setFilename(temp);
                        parent.startRecording();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        return builder.create();
    }

}
