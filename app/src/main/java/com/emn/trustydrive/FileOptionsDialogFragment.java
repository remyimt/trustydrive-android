package com.emn.trustydrive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class FileOptionsDialogFragment extends DialogFragment {

    private String[] fileOptions = new String[]{
            "Open",
            "Store one device",
            "Delete",
            "Rename",
            "Details"
    };

    private int filePosition;
    private DocumentMetadata file;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.file = DocumentListActivity.fakeDocuments.get(filePosition);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (file.isStoredOnDevice()) {
            fileOptions[1] = "Delete from device";
        }
        builder.setItems(fileOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 1:
                        ((DocumentListActivity) FileOptionsDialogFragment.this.getActivity()).storeOrDelete(getFilePosition());
                        break;
                    case 2:
                        ((DocumentListActivity) FileOptionsDialogFragment.this.getActivity()).deleteFile(getFilePosition());
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
        Intent intent = new Intent(getContext(), DocumentDetailsActivity.class);
        intent.putExtra("DOCUMENT_METADATA", getFilePosition());
        startActivity(intent);
    }

    public void setFilePosition(int filePosition) {
        this.filePosition = filePosition;
    }

    public int getFilePosition() {
        return filePosition;
    }
}