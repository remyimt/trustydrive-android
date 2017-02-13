package com.emn.trustydrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.emn.trustydrive.adapters.FileAdapter;
import com.emn.trustydrive.adapters.FolderAdapter;
import com.emn.trustydrive.metadata.FolderData;
import com.emn.trustydrive.metadata.TrustyDrive;

public class TestFoldersActivity extends AppCompatActivity {

    private FolderAdapter folderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_folders);
        folderAdapter = new FolderAdapter(this);
        ((ListView) findViewById(R.id.listView)).setAdapter(folderAdapter);
    }

    public void openFileOrFolder(View view) {
        int position = (int) view.getTag();
        Object item = folderAdapter.getItem(position);
        boolean isFolder = position < folderAdapter.getCurrentFolder().getFolders().size();
        if (isFolder) {
            folderAdapter.setCurrentFolder(item);
            folderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (folderAdapter.getCurrentFolder().getParentFolder() != null) {
            folderAdapter.setCurrentFolder(folderAdapter.getCurrentFolder().getParentFolder());
            folderAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }
}
