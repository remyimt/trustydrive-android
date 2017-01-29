package com.emn.trustydrive.tasks;

import android.os.AsyncTask;
import android.os.Environment;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.LoginActivity;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LoginTask extends AsyncTask<Object, Void, TrustyDrive> {
    private List<ChunkData> chunksData;
    private LoginActivity callingActivity;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(TrustyDrive metadata);

        void onError(List<Exception> e);
    }

    public LoginTask(List<ChunkData> chunksData, LoginActivity callingActivity, Callback callback) {
        this.chunksData = chunksData;
        this.callingActivity = callingActivity;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
        callingActivity.showLoading();
    }

    protected TrustyDrive doInBackground(Object... objects) {
        List<InputStream> files = new ArrayList<>();
        for (ChunkData chunkData : chunksData) {
            try {
                switch (chunkData.getAccount().getProvider()) {
                    case DROPBOX:
                        DbxClientV2 dbxClientV2 = new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                chunkData.getAccount().getToken());
                        if (!dbxClientV2.files().listFolder("").getEntries().isEmpty())
                            files.add(dbxClientV2.files().download("/" + chunkData.getName()).getInputStream());
                        break;
                }
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        try {
            if (files.size() > 0) {
                FileOutputStream fOut = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "fOut"));
                for (InputStream file : files) IOUtils.copy(file, fOut);
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
        else {
            for (ChunkData chunkData : chunksData)
                chunkData.getAccount().setMetadataFileName(chunkData.getName());
            callback.onTaskComplete(metadata);
        }
        callingActivity.dismissLoading();
    }
}
