package com.emn.trustydrive.tasks;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
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
    private Activity activity;
    private final Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(Uri uri);

        void onError(List<Exception> e);
    }

    public DownloadTask(List<ChunkData> chunksData, Activity activity, Callback callback) {
        this.chunksData = chunksData;
        this.activity = activity;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected Uri doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start download");
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
        Log.i(this.getClass().getSimpleName(), "Download finished");
        if (files.size() == chunksData.size())
            try {
                int size = files.size();
                FileOutputStream fOut = activity.openFileOutput("fOut.txt", Context.MODE_PRIVATE); //TODO
                int[] read = new int[size];
                int steps = 0;
                byte[][] buffers = new byte[size][16 * 1024]; // Can't use more
                while (-1 != (read[0] = files.get(0).read(buffers[0]))) {
                    steps++;
                    for (int i = 1; i < size; i++) read[i] = files.get(i).read(buffers[i]);
                    int totalRead = 0;
                    for (int i = 0; i < size; i++) totalRead += read[i];
                    byte[] buffer = new byte[totalRead];
                    for (int i = 0; i < totalRead; i++) buffer[i] = buffers[i % size][i / size];
                    fOut.write(buffer);
                }
                fOut.close();
                Log.i(this.getClass().getSimpleName(), "Finish reconstitute file in " + steps + " steps");
                return FileProvider.getUriForFile(activity, "com.emn.trustydrive.provider",
                        new File(activity.getFilesDir(), "fOut.txt")); //TODO
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
