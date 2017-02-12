package com.emn.trustydrive.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginTask extends AsyncTask<Object, Void, TrustyDrive> {
    private List<Account> accounts;
    private String password;
    private Activity activity;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(TrustyDrive metadata);

        void onError(List<Exception> e);
    }

    public LoginTask(List<Account> accounts, String password, Activity activity, Callback callback) {
        this.accounts = accounts;
        this.password = password;
        this.activity = activity;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected TrustyDrive doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start login");
        List<InputStream> files = new ArrayList<>();
        for (Account account : accounts) {
            try {
                switch (account.getProvider()) {
                    case DROPBOX:
                        DbxClientV2 dbxClientV2 = new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                account.getToken());
                        if (!dbxClientV2.files().listFolder("").getEntries().isEmpty())
                            files.add(dbxClientV2.files().download("/" + account.createHash(password)).getInputStream());
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
                FileOutputStream fOut = activity.openFileOutput("fOut", Context.MODE_PRIVATE);
                int[] read = new int[size];
                byte[][] buffers = new byte[size][16 * 1024]; // Can't use more
                while (-1 != (read[0] = files.get(0).read(buffers[0]))) {
                    for (int i = 1; i < size; i++) read[i] = files.get(i).read(buffers[i]);
                    int totalRead = 0;
                    for (int i = 0; i < size; i++) totalRead += read[i];
                    byte[] buffer = new byte[totalRead];
                    for (int i = 0; i < totalRead; i++) buffer[i] = buffers[i % size][i / size];
                    fOut.write(buffer);
                }
                fOut.close();
                Log.i(this.getClass().getSimpleName(), "File reconstitute");
                return new Gson().fromJson(IOUtils.toString(activity.openFileInput("fOut"),"UTF-8"), TrustyDrive.class);
            } catch (Exception e) {
                exceptions.add(e);
            }
        } else if (files.size() == 0 && exceptions.size() == 0) return new TrustyDrive();
        return null;
    }

    protected void onPostExecute(TrustyDrive metadata) {
        if (exceptions.size() > 0 || metadata == null)
            callback.onError(exceptions);
        else callback.onTaskComplete(metadata);
    }
}
