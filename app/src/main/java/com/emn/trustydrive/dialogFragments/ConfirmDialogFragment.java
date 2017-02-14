package com.emn.trustydrive.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.emn.trustydrive.FileListActivity;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.Type;

public class ConfirmDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String type = DataHolder.getInstance().getFile().getType() == Type.DIRECTORY ? "folder" : "file";
        return new AlertDialog.Builder(getActivity())
                .setTitle(DataHolder.getInstance().getFile().getName())
                .setMessage("Are you sure to want to delete this " + type + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((FileListActivity) getActivity()).delete();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}