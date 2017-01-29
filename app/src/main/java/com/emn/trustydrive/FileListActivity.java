package com.emn.trustydrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.emn.trustydrive.adapters.FileAdapter;
import com.emn.trustydrive.fragments.AddDialogFragment;
import com.emn.trustydrive.fragments.FileOptionsDialogFragment;
import com.emn.trustydrive.metadata.FileData;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileListActivity extends AppCompatActivity {
    public List<FileData> filesData = new ArrayList<>();
    private FileAdapter fileAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        for (int i = 1; i <= 15; i++)
            filesData.add(new FileData("File " + i, new Date(117, 0, i), 10));
        fileAdapter = new FileAdapter(FileListActivity.this, filesData);
        ((ListView) findViewById(R.id.listView)).setAdapter(fileAdapter);
    }

    public void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select a file to upload"), 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Toast.makeText(this, "TODO: Upload file", Toast.LENGTH_LONG).show(); //TODO
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings)
            startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void deleteFile(int position) {
        fileAdapter.deleteFile(position);
        fileAdapter.notifyDataSetChanged();
        Toast.makeText(FileListActivity.this, "File deleted", Toast.LENGTH_SHORT).show();
    }

    public void displayFileOptions(View view) {
        FileOptionsDialogFragment dialog = new FileOptionsDialogFragment();
        dialog.setFilePosition((int) view.getTag());
        dialog.setFileData(filesData.get((int) view.getTag()));
        dialog.show(getFragmentManager(), null);
    }

    public void toggleSavedOnDeviceStatus(int filePosition) {
        FileData fileData = fileAdapter.getFilesData().get(filePosition);
        //TODO
        fileAdapter.notifyDataSetChanged();
        Toast.makeText(FileListActivity.this, "TODO: Save file", Toast.LENGTH_SHORT).show();
    }

    public void openFile(View view) {
        FileData fileData = fileAdapter.getFilesData().get((int) view.getTag());
        openFile(fileData);
    }

    public void openFile(FileData fileData) {
        Toast.makeText(FileListActivity.this, "TODO: Open file", Toast.LENGTH_SHORT).show(); //TODO
    }

    public void displayAddOptions(View view) {
        new AddDialogFragment().show(getFragmentManager(), null);
    }
}
