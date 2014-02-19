package com.recording;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.notetaker.R;

/**
 * Created by Dylan on 2/19/14.
 */
public class FileNameDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle SavedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater lf = getActivity().getLayoutInflater();
        builder.setView(lf.inflate(R.layout.dialog_filename, null)); 
        builder.setTitle("Select File")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
