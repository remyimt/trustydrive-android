package com.emn.trustydrive.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.ChunkData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadTask extends AsyncTask<Object, Void, Void> {
    private InputStream inputStream;
    private List<ChunkData> chunksData;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete();

        void onError(List<Exception> exceptions);
    }

    public UploadTask(InputStream inputStream, List<ChunkData> chunksData, Callback callback) {
        this.inputStream = inputStream;
        this.chunksData = chunksData;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected Void doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start");
        int size = chunksData.size();
        int steps = 0;
        ByteArrayInputStream[] chunks = new ByteArrayInputStream[size];
        try {
            ByteArrayOutputStream[] chunksOut = new ByteArrayOutputStream[size];
            for (int i = 0; i < size; i++) chunksOut[i] = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 1024];
            int read;
            while (-1 != (read = inputStream.read(buffer))) {
                byte[][] outBuffers = new byte[size][];
                for (int i = 0; i < size; i++)
                    outBuffers[i] = new byte[read / size + (read % size - i > 0 ? 1 : 0)];
                for (int i = 0; i < read; i++) outBuffers[i % size][i / size] = buffer[i];
                for (int i = 0; i < size; i++) chunksOut[i].write(outBuffers[i]);
                steps++;
            }
            for (int i = 0; i < chunks.length; i++)
                chunks[i] = new ByteArrayInputStream(chunksOut[i].toByteArray());
        } catch (IOException e) {
            exceptions.add(e);
        }
        Log.i(this.getClass().getSimpleName(), "Break data finished in " + steps + " steps");
        for (int i = 0; i < size; i++) {
            ChunkData chunkData = chunksData.get(i);
            try {
                switch (chunkData.getAccount().getProvider()) {
                    case DROPBOX:
                        new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                chunkData.getAccount().getToken()).files().uploadBuilder("/"
                                + chunkData.getName()).uploadAndFinish(chunks[i]);
                        break;
                }
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        Log.i(this.getClass().getSimpleName(), "End");
        return null;
    }

    protected void onPostExecute(Void v) {
        if (exceptions.size() > 0) callback.onError(exceptions);
        else new UpdateTask(new UpdateTask.Callback() {
            public void onTaskComplete() {
                callback.onTaskComplete();
            }

            public void onError(List<Exception> exceptions) {
                callback.onError(exceptions);
            }
        }).execute();
    }
}
