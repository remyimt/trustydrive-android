package com.emn.trustydrive.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        editText.setSingleLine();
        editText.requestFocus();
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Rename " + (fileData.getType() == Type.DIRECTORY ? "folder" : "file"))
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((FileListActivity) getActivity()).rename(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            public void afterTextChanged(Editable editable) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(editable.length() > 0);
            }
        });
        return dialog;
    }


}