package com.emn.trustydrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
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
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.FileData;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.emn.trustydrive.metadata.Type;
import com.emn.trustydrive.tasks.DeleteTask;
import com.emn.trustydrive.tasks.DownloadTask;
import com.emn.trustydrive.tasks.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileListActivity extends AppCompatActivity {
    private FileAdapter fileAdapter;
    private ArrayList<Account> accounts;
    private TrustyDrive metadata;
    private ProgressDialog progress;
    private List<FileData> path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        metadata = DataHolder.getInstance().getMetadata();
        if (metadata == null) onErrors(new ArrayList<Exception>());
        else {
            path = new ArrayList<>();
            fileAdapter = new FileAdapter(FileListActivity.this, metadata.getFiles());
            ((ListView) findViewById(R.id.listView)).setAdapter(fileAdapter);
        }
    }

    protected void onResume() {
        super.onResume();
        accounts = DataHolder.getInstance().getAccounts();
    }

    public void chooseFile() {
        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("*/*"), 0);
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
                final FileData fileData = new FileData(returnCursor.getString(nameIndex), new Date().getTime(),
                        Type.FILE, "", returnCursor.getInt(sizeIndex), chunksData, null);
                if (path.isEmpty()) metadata.getFiles().add(fileData);
                else path.get(path.size() - 1).getFiles().add(fileData);
                this.showLoading();
                new UploadTask(inputStream, chunksData, metadata, accounts, this, new UploadTask.Callback() {
                    public void onTaskComplete() {
                        onSuccess("Upload succeed");
                    }

                    public void onError(List<Exception> exceptions) {
                        onErrors(exceptions);
                    }
                }).execute();
            } catch (FileNotFoundException e) {
                Toast.makeText(FileListActivity.this, "File not found", Toast.LENGTH_SHORT).show();
            }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
            startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void deleteFile(final int position) {
        //TODO: Add confirmation window
        FileData removedFile;
        if (path.isEmpty()) removedFile = metadata.getFiles().remove(position);
        else removedFile = path.get(path.size() - 1).getFiles().remove(position);
        this.showLoading();
        if (removedFile.getType() == Type.DIRECTORY)
            //TODO: Implement recursive delete
            new UploadTask(null, null, metadata, accounts, this, new UploadTask.Callback() {
                public void onTaskComplete() {
                    onSuccess("Folder deleted");
                }

                public void onError(List<Exception> exceptions) {
                    onErrors(exceptions);
                }
            }).execute();
        else
            new DeleteTask(removedFile.getChunks(), metadata, accounts, this, new DeleteTask.Callback() {
                public void onTaskComplete() {
                    onSuccess("File deleted");
                }

                public void onError(List<Exception> exceptions) {
                    onErrors(exceptions);
                }
            }).execute();
    }

    public void displayFileOptions(View view) {
        DataHolder.getInstance().setFile(fileAdapter.getItem((int) view.getTag()));
        FileOptionsDialogFragment dialog = new FileOptionsDialogFragment();
        dialog.setFilePosition((int) view.getTag());
        dialog.show(getFragmentManager(), null);
    }

    public void toggleOnDeviceStatus(int filePosition) {
        FileData fileData = fileAdapter.getItem(filePosition);
        //TODO
        fileAdapter.notifyDataSetChanged();
        Toast.makeText(FileListActivity.this, "TODO: Save file", Toast.LENGTH_SHORT).show();
    }

    public void openFile(View view) {
        openFile(fileAdapter.getItem((int) view.getTag()));
    }

    public void openFile(FileData fileData) {
        if (fileData.getType() == Type.DIRECTORY) {
            path.add(fileData);
            setTitle(fileData.getName());
            fileAdapter.setFiles(fileData.getFiles());
            fileAdapter.notifyDataSetChanged();
        } else {
            this.showLoading();
            new DownloadTask(fileData.getChunks(), this, new DownloadTask.Callback() {
                public void onTaskComplete(Uri uri) {
                    progress.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(uri, "*/*"); //TODO
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else Toast.makeText(FileListActivity.this, "Not app found to open this file",
                            Toast.LENGTH_SHORT).show();
                }

                public void onError(List<Exception> exceptions) {
                    onErrors(exceptions);
                }
            }).execute();
        }
    }

    public void createDirectory() {
        final FileData fileData = new FileData("new folder", new Date().getTime(),
                Type.DIRECTORY, "", 0, null, new ArrayList<FileData>());
        if (path.isEmpty()) metadata.getFiles().add(fileData);
        else path.get(path.size() - 1).getFiles().add(fileData);
        this.showLoading();
        new UploadTask(null, null, metadata, accounts, this, new UploadTask.Callback() {
            public void onTaskComplete() {
                onSuccess("Folder created");
            }

            public void onError(List<Exception> exceptions) {
                onErrors(exceptions);
            }
        }).execute();
    }

    public void displayAddOptions(View view) {
        new AddDialogFragment().show(getFragmentManager(), null);
    }

    public String generateRandomHash() {
        // TODO: Improve it
        SecureRandom random = new SecureRandom();
        return new BigInteger(40, random).toString(16);
    }

    public void showLoading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    public void onErrors(List<Exception> exceptions) {
        for (Exception exception : exceptions) exception.printStackTrace(); //TODO
        if (progress != null) progress.dismiss();
        Toast.makeText(FileListActivity.this, "Error", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public void onSuccess(String message) {
        if (progress != null) progress.dismiss();
        fileAdapter.notifyDataSetChanged();
        Toast.makeText(FileListActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        if (path.isEmpty()) super.onBackPressed();
        else {
            path.remove(path.size() - 1);
            if (path.isEmpty()) {
                setTitle(R.string.app_name);
                fileAdapter.setFiles(metadata.getFiles());
            } else {
                setTitle(path.get(path.size() - 1).getName());
                fileAdapter.setFiles(path.get(path.size() - 1).getFiles());
            }
            fileAdapter.notifyDataSetChanged();
        }
    }
}
