package com.emn.trustydrive.tasks;

import android.os.AsyncTask;
import android.os.Environment;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.ChunkData;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<Object, Void, File> {
    private List<ChunkData> chunksData;
    private final Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(File file);

        void onError(List<Exception> e);
    }

    public DownloadTask(List<ChunkData> chunksData, Callback callback) {
        this.chunksData = chunksData;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected File doInBackground(Object... objects) {
        List<InputStream> files = new ArrayList<>();
        for (ChunkData chunkData : chunksData) {
            try {
                switch (chunkData.getAccount().getProvider()) {
                    case DROPBOX:
                        files.add(new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                chunkData.getAccount().getToken()).files()
                                .download("/" + chunkData.getName()).getInputStream());
                        break;
                }
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        try {
            FileOutputStream fOut = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "fOut"));
            for (InputStream file : files) IOUtils.copy(file, fOut);
            fOut.close();
            return new File(Environment.getExternalStorageDirectory(), "fOut");
        } catch (Exception e) {
            exceptions.add(e);
        }
        return null;
    }

    protected void onPostExecute(File file) {
        if (exceptions.size() > 0 || file == null) callback.onError(exceptions);
        else callback.onTaskComplete(file);
    }
}
