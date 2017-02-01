package com.emn.trustydrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.emn.trustydrive.adapters.FileAdapter;
import com.emn.trustydrive.fragments.AddDialogFragment;
import com.emn.trustydrive.fragments.FileOptionsDialogFragment;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.FileData;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.emn.trustydrive.tasks.DownloadTask;
import com.emn.trustydrive.tasks.UploadTask;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileListActivity extends AppCompatActivity {
    private FileAdapter fileAdapter;
    private List<Account> accounts;
    private TrustyDrive metadata;
    private ProgressDialog progress;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        accounts = getIntent().getExtras().getParcelableArrayList("accounts");
        metadata = getIntent().getExtras().getParcelable("metadata");
        fileAdapter = new FileAdapter(FileListActivity.this, metadata);
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
                Cursor returnCursor = getContentResolver().query(data.getData(), null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                List<ChunkData> chunksData = new ArrayList<>(accounts.size());
                for (Account account : accounts)
                    chunksData.add(new ChunkData(account, this.generateRandomHash()));
                fileAdapter.add(new FileData(returnCursor.getString(nameIndex),
                        new Date(), "", returnCursor.getInt(sizeIndex), chunksData));
                fileAdapter.notifyDataSetChanged();
                new UploadTask(inputStream, chunksData, metadata, new UploadTask.Callback() {
                    public void onTaskComplete() {
                        Toast.makeText(FileListActivity.this, "Upload succeed", Toast.LENGTH_SHORT).show();
                    }

                    public void onError(List<Exception> exceptions) {
                        for (Exception exception : exceptions) exception.printStackTrace(); //TODO
                    }
                }).execute();
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
        dialog.setFileData(metadata.getFilesData().get((int) view.getTag()));
        dialog.show(getFragmentManager(), null);
    }

    public void toggleSavedOnDeviceStatus(int filePosition) {
        FileData fileData = fileAdapter.getFilesData().get(filePosition);
        //TODO
        fileAdapter.notifyDataSetChanged();
        Toast.makeText(FileListActivity.this, "TODO: Save file", Toast.LENGTH_SHORT).show();
    }

    public void openFile(View view) {
        openFile(fileAdapter.getFilesData().get((int) view.getTag()));
    }

    public void openFile(FileData fileData) {
        this.showLoading();
        new DownloadTask(fileData.getChunksData(), new DownloadTask.Callback() {
            public void onTaskComplete(Uri uri) {
                progress.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setType("*/*"); //TODO
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, "Open with :"));
                } else Toast.makeText(FileListActivity.this, "Not app found to open this file",
                        Toast.LENGTH_SHORT).show();
            }

            public void onError(List<Exception> exceptions) {
                Toast.makeText(FileListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                for (Exception exception : exceptions) exception.printStackTrace(); //TODO
                progress.dismiss();
            }
        }).execute();
    }

    public void displayAddOptions(View view) {
        new AddDialogFragment().show(getFragmentManager(), null);
    }

    public String generateRandomHash() {
        // TODO: Improve it
        SecureRandom random = new SecureRandom();
        String hash = new BigInteger(40, random).toString(16);
        Log.e("hash", hash);
        return hash;
    }

    public void showLoading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
}
