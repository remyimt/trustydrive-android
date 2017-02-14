package com.emn.trustydrive.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.emn.trustydrive.FileListActivity;

public class AddDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setItems(new String[]{"Upload a file", "Create a folder"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        ((FileListActivity) getActivity()).chooseFile();
                                        break;
                                    case 1:
                                        ((FileListActivity) getActivity()).createDirectory();
                                        break;
                                }
                            }
                        }).create();
    }
}