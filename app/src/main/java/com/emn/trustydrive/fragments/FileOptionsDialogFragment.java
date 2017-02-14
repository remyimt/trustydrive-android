package com.emn.trustydrive.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.emn.trustydrive.FileDetailsActivity;
import com.emn.trustydrive.FileListActivity;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.FileData;
import com.emn.trustydrive.metadata.Type;

public class FileOptionsDialogFragment extends DialogFragment {
    private String[] fileOptions = new String[]{"Open", "Rename", "Delete", "Save on device", "Details"};
    private int filePosition;

    public int getFilePosition() {
        return filePosition;
    }

    public void setFilePosition(int filePosition) {
        this.filePosition = filePosition;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final FileData fileData =  DataHolder.getInstance().getFile();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (fileData.isOnDevice()) fileOptions[3] = "Delete from device";
        if(fileData.getType() == Type.DIRECTORY) fileOptions = new String[]{"Open", "Rename", "Delete"};
        builder.setItems(fileOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ((FileListActivity) FileOptionsDialogFragment.this.getActivity()).openFile(fileData);
                        break;
                    case 2:
                        ((FileListActivity) FileOptionsDialogFragment.this.getActivity()).deleteFile(getFilePosition());
                        break;
                    case 3:
                        ((FileListActivity) FileOptionsDialogFragment.this.getActivity()).toggleOnDeviceStatus(getFilePosition());
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), FileDetailsActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }
}
