package com.emn.trustydrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emn.trustydrive.adapters.FileAdapter;
import com.emn.trustydrive.dialogFragments.AddDialogFragment;
import com.emn.trustydrive.dialogFragments.OptionDialogFragment;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.FileData;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.emn.trustydrive.metadata.Type;
import com.emn.trustydrive.tasks.DeleteTask;
import com.emn.trustydrive.tasks.DownloadTask;
import com.emn.trustydrive.tasks.UpdateTask;
import com.emn.trustydrive.tasks.UploadTask;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.makeText;

public class FileListActivity extends AppCompatActivity {
    private FileAdapter fileAdapter;
    private ArrayList<Account> accounts;
    private TrustyDrive metadata;
    private ProgressDialog progress;
    private List<FileData> path;
    private List<String> onDevice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        metadata = DataHolder.getInstance().getMetadata();
        if (metadata == null) onErrors(new ArrayList<Exception>());
        else {
            path = new ArrayList<>();
//            for (File f : getFilesDir().listFiles()) f.delete();
            onDevice = Arrays.asList(fileList());
            checkOnDevice(metadata.getFiles());
            fileAdapter = new FileAdapter(FileListActivity.this, metadata.getFiles());
            ListView listView = ((ListView) findViewById(R.id.listView));
            listView.setAdapter(fileAdapter);
            listView.setLongClickable(true);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    displayOptions(view);
                    return true;
                }
            });
        }
    }

    private void checkOnDevice(ArrayList<FileData> files) {
        for (FileData fileData : files) {
            if (fileData.getType() == Type.DIRECTORY) {
                checkOnDevice(fileData.getFiles());
            } else {
                if (!fileData.getChunks().isEmpty())
                    fileData.setOnDevice(onDevice.contains(fileData.getChunks().get(0).getName()));
            }
        }
    }

    protected void onResume() {
        super.onResume();
        accounts = DataHolder.getInstance().getAccounts();
        Log.e("files", fileList().length + "");
        for (String s : fileList()) Log.e("filename", s);
        Log.e("cache", getCacheDir().listFiles().length + "");
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
                new UploadTask(inputStream, chunksData, new UploadTask.Callback() {
                    public void onTaskComplete() {
                        onSuccess("Upload succeed");
                    }

                    public void onError(List<Exception> exceptions) {
                        onErrors(exceptions);
                    }
                }).execute();
            } catch (FileNotFoundException e) {
                makeText(FileListActivity.this, "File not found", Toast.LENGTH_SHORT).show();
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

    public void delete() {
        FileData toRemove = DataHolder.getInstance().getFile();
        if (path.isEmpty()) metadata.getFiles().remove(toRemove);
        else path.get(path.size() - 1).getFiles().remove(toRemove);
        this.showLoading();
        if (toRemove.getType() == Type.DIRECTORY)
            //TODO: Implement recursive delete
            new UpdateTask(new UpdateTask.Callback() {
                public void onTaskComplete() {
                    onSuccess("Folder deleted");
                }

                public void onError(List<Exception> exceptions) {
                    onErrors(exceptions);
                }
            }).execute();
        else
            new DeleteTask(toRemove.getChunks(), new DeleteTask.Callback() {
                public void onTaskComplete() {
                    onSuccess("File deleted");
                }

                public void onError(List<Exception> exceptions) {
                    onErrors(exceptions);
                }
            }).execute();
    }

    public void displayOptions(View view) {
        DataHolder.getInstance().setFile(fileAdapter.getItem((int) view.getTag()));
        new OptionDialogFragment().show(getFragmentManager(), null);
    }

    public void toggleOnDeviceStatus() {
        FileData fileData = DataHolder.getInstance().getFile();
        if (fileData.isOnDevice()) {
            fileData.setOnDevice(false);
            if (deleteFile(fileData.getChunks().get(0).getName()))
                onSuccess("File removed from device");
            else onSuccess("File already remove from device");
        } else {
            boolean inCache = false;
            for (File file : getCacheDir().listFiles()) {
                if (file.getName().equals(fileData.getChunks().get(0).getName())) {
                    inCache = true;
                    try {
                        OutputStream output = openFileOutput(file.getName(), Context.MODE_PRIVATE);
                        InputStream input = new FileInputStream(file);
                        this.showLoading();
                        IOUtils.copy(input, output);
                        file.delete();
                        fileData.setOnDevice(true);
                        onSuccess("File saved");
                    } catch (Exception e) {
                        onErrors(Collections.singletonList(e));
                    }
                }
            }
            if (!inCache) {
                this.showLoading();
                new DownloadTask(fileData.getChunks(), false, this, new DownloadTask.Callback() {
                    public void onTaskComplete(Uri uri) {
                        DataHolder.getInstance().getFile().setOnDevice(true);
                        onSuccess("File saved");
                    }

                    public void onError(List<Exception> exceptions) {
                        onErrors(exceptions);
                    }
                }).execute();
            }
        }
    }

    public void open(View view) {
        open(fileAdapter.getItem((int) view.getTag()));
    }

    public void open(FileData fileData) {
        if (fileData.getType() == Type.DIRECTORY) {
            path.add(fileData);
            setTitle(fileData.getName());
            fileAdapter.setFiles(fileData.getFiles());
            fileAdapter.notifyDataSetChanged();
        } else {
            if (!fileData.getChunks().isEmpty()) {
                String name = fileData.getChunks().get(0).getName();
                if (getFileStreamPath(name).exists())
                    open(FileProvider.getUriForFile(this, "com.emn.trustydrive.provider",
                            new File(getFilesDir(), name)));
                else if (new File(getCacheDir(), name).exists())
                    open(FileProvider.getUriForFile(this, "com.emn.trustydrive.provider",
                            new File(getCacheDir(), name)));
                else {
                    this.showLoading();
                    new DownloadTask(fileData.getChunks(), true, this, new DownloadTask.Callback() {
                        public void onTaskComplete(Uri uri) {
                            progress.dismiss();
                            open(uri);
                        }

                        public void onError(List<Exception> exceptions) {
                            onErrors(exceptions);
                        }
                    }).execute();
                }
            }
        }
    }

    public void open(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(uri, "*/*"); //TODO
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else makeText(FileListActivity.this, "Not app found to open this file",
                Toast.LENGTH_SHORT).show();
    }

    public void createDirectory() {
        final FileData fileData = new FileData("new folder", new Date().getTime(),
                Type.DIRECTORY, "", 0, null, new ArrayList<FileData>());
        if (path.isEmpty()) metadata.getFiles().add(fileData);
        else path.get(path.size() - 1).getFiles().add(fileData);
        this.showLoading();
        new UpdateTask(new UpdateTask.Callback() {
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
        makeText(FileListActivity.this, "Error", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public void onSuccess(String message) {
        if (progress != null) progress.dismiss();
        fileAdapter.notifyDataSetChanged();
        makeText(FileListActivity.this, message, Toast.LENGTH_SHORT).show();
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

    public void rename(String name) {
        if (!name.equals(DataHolder.getInstance().getFile().getName())) {
            this.showLoading();
            DataHolder.getInstance().getFile().setName(name);
            new UpdateTask(new UpdateTask.Callback() {
                public void onTaskComplete() {
                    onSuccess("File renamed");
                }

                public void onError(List<Exception> exceptions) {
                    onErrors(exceptions);
                }
            }).execute();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        for (File f : getCacheDir().listFiles()) f.delete();
    }
}
