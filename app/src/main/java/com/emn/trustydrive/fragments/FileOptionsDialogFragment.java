package com.emn.trustydrive.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.emn.trustydrive.FileDetailsActivity;
import com.emn.trustydrive.FileListActivity;
import com.emn.trustydrive.metadata.FileData;

public class FileOptionsDialogFragment extends DialogFragment {
    private String[] fileOptions = new String[]{"Open", "Save on device", "Delete", "Rename", "Details"};
    private int filePosition;
    private FileData fileData;

    public int getFilePosition() {
        return filePosition;
    }

    public void setFilePosition(int filePosition) {
        this.filePosition = filePosition;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (false) fileOptions[1] = "Delete from device"; //TODO
        builder.setItems(fileOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ((FileListActivity) FileOptionsDialogFragment.this.getActivity()).openFile(fileData);
                        break;
                    case 1:
                        ((FileListActivity) FileOptionsDialogFragment.this.getActivity()).toggleSavedOnDeviceStatus(getFilePosition());
                        break;
                    case 2:
                        ((FileListActivity) FileOptionsDialogFragment.this.getActivity()).deleteFile(getFilePosition());
                        break;
                    case 4:
                        showFileDetails();
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }

    private void showFileDetails() {
        Intent intent = new Intent(getActivity(), FileDetailsActivity.class);
        intent.putExtra("fileData", fileData);
        startActivity(intent);
    }
}