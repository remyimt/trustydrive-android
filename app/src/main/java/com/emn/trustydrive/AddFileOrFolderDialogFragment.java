package com.emn.trustydrive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class AddFileOrFolderDialogFragment extends DialogFragment {

    private String[] fileOptions = new String[]{
            "Upload a file",
            "Create a folder"
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(fileOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(AddFileOrFolderDialogFragment.this.getActivity(), "Todo", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(AddFileOrFolderDialogFragment.this.getActivity(), "Todo", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }
}