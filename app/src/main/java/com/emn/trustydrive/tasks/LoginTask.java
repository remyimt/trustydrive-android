package com.emn.trustydrive.tasks;

import android.os.AsyncTask;
import android.os.Environment;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LoginTask extends AsyncTask<Object, Void, TrustyDrive> {
    private List<Account> accounts;
    private String password;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(TrustyDrive metadata);

        void onError(List<Exception> e);
    }

    public LoginTask(List<Account> accounts, String password, Callback callback) {
        this.accounts = accounts;
        this.password = password;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected TrustyDrive doInBackground(Object... objects) {
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
        try {
            if (files.size() > 0) {
                FileOutputStream fOut = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "fOut"));
                int byt;
                int i = 0;
                while (-1 != (byt = files.get(i%files.size()).read())) {
                    fOut.write(byt);
                    i++;
                }
                fOut.close();
                return new Gson().fromJson(FileUtils.readFileToString(new File(Environment.getExternalStorageDirectory(),
                        "fOut"), StandardCharsets.UTF_8), TrustyDrive.class);
            } else if (files.size() == 0 && exceptions.size() == 0) return new TrustyDrive();
        } catch (Exception e) {
            exceptions.add(e);
        }
        return null;
    }

    protected void onPostExecute(TrustyDrive metadata) {
        if (exceptions.size() > 0 || metadata == null)
            callback.onError(exceptions);
        else callback.onTaskComplete(metadata);
    }
}
