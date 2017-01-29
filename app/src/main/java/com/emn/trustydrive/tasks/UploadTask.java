package com.emn.trustydrive.tasks;

import android.os.AsyncTask;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.ChunkData;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadTask extends AsyncTask<Object, Void, Void> {
    private InputStream inputStream;
    private List<ChunkData> chunksData;
    private final Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete();

        void onError(List<Exception> exceptions);
    }

    public UploadTask(InputStream inputStream, List<ChunkData> chunksData, Callback callback) {
        this.inputStream = inputStream;
        this.chunksData = chunksData;
        this.callback = callback;
        this.exceptions  = new ArrayList<>();
    }

    protected Void doInBackground(Object... objects) {
        FileInputStream[] chunks = new FileInputStream[chunksData.size()];
        //TODO: Split inputStream
        //TODO: Update metadata (if it's not metadata)
        for (int i = 0; i < chunksData.size(); i++) {
            ChunkData chunkData = chunksData.get(i);
            try {
                switch (chunkData.getAccount().getProvider()) {
                    case DROPBOX:
                        new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                chunkData.getAccount().getToken()).files().uploadBuilder("/" + chunkData.getName())
                                .uploadAndFinish(chunks[i]);
                        break;
                }
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        return null;
    }

    protected void onPostExecute(Void v) {
        if (exceptions.size() > 0) callback.onError(exceptions);
        else callback.onTaskComplete();
    }
}
