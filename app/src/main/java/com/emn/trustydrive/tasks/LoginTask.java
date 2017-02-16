package com.emn.trustydrive.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginTask extends AsyncTask<Object, Void, TrustyDrive> {
    private String password;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(TrustyDrive metadata);

        void onError(List<Exception> e);
    }

    public LoginTask(String password, Callback callback) {
        this.password = password;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected TrustyDrive doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start");
        List<InputStream> files = new ArrayList<>();
        TrustyDrive metadata = null;
        for (Account account : DataHolder.getInstance().getAccounts()) {
            account.setMetadataFileName(account.createHash(password));
            try {
                switch (account.getProvider()) {
                    case DROPBOX:
                        DbxClientV2 dbxClientV2 = new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                account.getToken());
                        if (!dbxClientV2.files().listFolder("").getEntries().isEmpty())
                            files.add(dbxClientV2.files().download("/" + account.getMetadataFileName()).getInputStream());
                        break;
                }
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        if (files.size() > 0) {
            Log.i(this.getClass().getSimpleName(), "Chunks found");
            try {
                int size = files.size();
                ByteArrayOutputStream metadataBytes = new ByteArrayOutputStream();
                int[] read = new int[size];
                byte[][] buffers = new byte[size][16 * 1024]; // Can't use more
                while (-1 != (read[0] = files.get(0).read(buffers[0]))) {
                    for (int i = 1; i < size; i++) read[i] = files.get(i).read(buffers[i]);
                    int totalRead = 0;
                    for (int i = 0; i < size; i++) totalRead += read[i];
                    byte[] buffer = new byte[totalRead];
                    for (int i = 0; i < totalRead; i++) buffer[i] = buffers[i % size][i / size];
                    metadataBytes.write(buffer);
                }
                metadata = new Gson().fromJson(metadataBytes.toString(), TrustyDrive.class);
            } catch (Exception e) {
                exceptions.add(e);
            }
        } else if (files.size() == 0 && exceptions.size() == 0){
            Log.i(this.getClass().getSimpleName(), "Create new metadata");
            metadata = new TrustyDrive();
        }
        Log.i(this.getClass().getSimpleName(), "End");
        return metadata;
    }

    protected void onPostExecute(TrustyDrive metadata) {
        if (exceptions.size() > 0 || metadata == null) callback.onError(exceptions);
        else callback.onTaskComplete(metadata);
    }
}
