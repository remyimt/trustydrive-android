package com.emn.trustydrive.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.ChunkData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<Object, Void, Uri> {
    private List<ChunkData> chunksData;
    private final Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(Uri uri);

        void onError(List<Exception> e);
    }

    public DownloadTask(List<ChunkData> chunksData, Callback callback) {
        this.chunksData = chunksData;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected Uri doInBackground(Object... objects) {
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
        if (files.size() == chunksData.size())
            try {
                FileOutputStream fOut = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "fOut2"));
                int byt;
                int i = 0;
                while (-1 != (byt = files.get(i % files.size()).read())) {
                    fOut.write(byt);
                    i++;
                }
                Log.e("bytes read", ""+i); // Always the same number read
                fOut.close();
                return Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "fOut"));
            } catch (Exception e) {
                exceptions.add(e);
            }
        return null;
    }

    protected void onPostExecute(Uri uri) {
        if (exceptions.size() > 0 || uri == null) callback.onError(exceptions);
        else callback.onTaskComplete(uri);
    }
}
