package com.emn.trustydrive.dialogFragments;

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

public class OptionDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] options;
        final FileData fileData = DataHolder.getInstance().getFile();
        if (fileData.getType() == Type.FILE)
            options = new String[]{"Open", "Rename", "Move", "Delete",
                    fileData.isOnDevice() ? "Delete from device" : "Save on device", "Details"};
        else options = new String[]{"Open", "Rename", "Move", "Delete"};
        return new AlertDialog.Builder(getActivity())
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ((FileListActivity) getActivity()).open(fileData);
                                break;
                            case 1:
                                new RenameDialogFragment().show(getFragmentManager(), null);
                                break;
                            case 2:
                                //TODO
                                break;
                            case 3:
                                new ConfirmDialogFragment().show(getFragmentManager(), null);
                                break;
                            case 4:
                                ((FileListActivity) getActivity()).toggleOnDeviceStatus();
                                break;
                            case 5:
                                startActivity(new Intent(getActivity(), FileDetailsActivity.class));
                                break;
                        }
                    }
                }).setTitle(fileData.getName()).create();
    }
}
