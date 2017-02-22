package com.emn.trustydrive.tasks;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.ChunkData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<Object, Void, Uri> {
    private List<ChunkData> chunksData;
    private boolean inCache;
    private Activity activity;
    private final Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete(Uri uri);

        void onError(List<Exception> e);
    }

    public DownloadTask(List<ChunkData> chunksData, boolean inCache, Activity activity, Callback callback) {
        this.chunksData = chunksData;
        this.inCache = inCache;
        this.activity = activity;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected Uri doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start");
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
        if (!files.isEmpty() && files.size() == chunksData.size())
            try {
                int size = files.size();
                FileOutputStream fOut = new FileOutputStream(new File(inCache ?
                        activity.getCacheDir() : activity.getFilesDir(), chunksData.get(0).getName()));
                List<BufferedInputStream> buffers = new ArrayList<>();
                for (InputStream file : files) buffers.add(new BufferedInputStream(file));
                int i = 0;
                int read;
                List<Byte> buffer = new ArrayList<>();
                while (-1 != (read = buffers.get(i%size).read())) {
                    buffer.add((byte) read);
                    i++;
                }
                byte[] bufferArray = new byte[buffer.size()];
                Log.e("i is", i+"");
                for (i = 0; i < buffer.size(); i++) bufferArray[i] = buffer.get(i);
                fOut.write(bufferArray);
                fOut.close();
                Log.i(this.getClass().getSimpleName(), "End");
                return FileProvider.getUriForFile(activity, "com.emn.trustydrive.provider",
                        new File(inCache ? activity.getCacheDir() : activity.getFilesDir(), chunksData.get(0).getName()));
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
