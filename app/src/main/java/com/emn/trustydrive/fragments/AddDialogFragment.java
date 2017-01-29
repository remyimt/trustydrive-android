package com.emn.trustydrive.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.emn.trustydrive.FileListActivity;

public class AddDialogFragment extends DialogFragment {
    private String[] fileOptions = new String[]{"Upload a file", "Create a folder"};

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(fileOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ((FileListActivity) AddDialogFragment.this.getActivity()).chooseFile();
                        break;
                    case 1:
                        Toast.makeText(AddDialogFragment.this.getActivity(), "TODO", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }
}