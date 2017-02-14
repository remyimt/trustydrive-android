package com.emn.trustydrive.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;

import com.emn.trustydrive.FileListActivity;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.FileData;
import com.emn.trustydrive.metadata.Type;

public class RenameDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FileData fileData = DataHolder.getInstance().getFile();
        final EditText editText = new EditText(getActivity());
        editText.setText(fileData.getName());
        editText.requestFocus();
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Rename " + (fileData.getType() == Type.DIRECTORY ? "folder" : "file"))
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((FileListActivity) getActivity()).rename(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }
}