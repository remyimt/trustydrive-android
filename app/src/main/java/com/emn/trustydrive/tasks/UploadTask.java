package com.emn.trustydrive.tasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UploadTask extends AsyncTask<Object, Void, Void> {
    private InputStream inputStream;
    private List<ChunkData> chunksData;
    private TrustyDrive metadata;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete();

        void onError(List<Exception> exceptions);
    }

    public UploadTask(InputStream inputStream, List<ChunkData> chunksData, TrustyDrive metadata, Callback callback) {
        this.inputStream = inputStream;
        this.chunksData = chunksData;
        this.metadata = metadata;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected Void doInBackground(Object... objects) {
        if (inputStream != null) this.upload(inputStream);
        for (ChunkData chunkData : chunksData)
            chunkData.setName(chunkData.getAccount().getMetadataFileName());
        Log.e("metadata", new Gson().toJson(metadata));
        this.upload(new ByteArrayInputStream(new Gson().toJson(metadata).getBytes(StandardCharsets.UTF_8)));
        return null;
    }

    protected void onPostExecute(Void v) {
        if (exceptions.size() > 0) callback.onError(exceptions);
        else callback.onTaskComplete();
    }

    private void upload(InputStream inputStream) {
        int size = chunksData.size();
        FileInputStream[] chunks = new FileInputStream[size];
        try {
            FileOutputStream[] chunksOut = new FileOutputStream[size];
            for (int i = 0; i < size; i++)
                chunksOut[i] = new FileOutputStream(new File(Environment.getExternalStorageDirectory(),
                        chunksData.get(i).getName()));
            byte[] buffer = new byte[1024 * 1024];
            int read;
            while (-1 != (read = inputStream.read(buffer))) {
                byte[][] outBuffers = new byte[size][];
                for (int i = 0; i < size; i++) outBuffers[i] = new byte[read/size + (read%size - i > 0 ? 1 : 0)];
                for (int i = 0; i < read; i++) outBuffers[i % size][i / size] = buffer[i];
                for (int i = 0; i < size; i++) chunksOut[i].write(outBuffers[i]);
            }
            for (FileOutputStream chunk : chunksOut) chunk.close();
            for (int j = 0; j < size; j++)
                chunks[j] = new FileInputStream(new File(Environment.getExternalStorageDirectory(),
                        chunksData.get(j).getName()));
        } catch (IOException e) {
            exceptions.add(e);
        }
        for (int i = 0; i < size; i++) {
            ChunkData chunkData = chunksData.get(i);
            try {
                switch (chunkData.getAccount().getProvider()) {
                    case DROPBOX:
                        new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                chunkData.getAccount().getToken()).files().uploadBuilder("/"
                                + chunkData.getName()).withMode(WriteMode.OVERWRITE)
                                .uploadAndFinish(chunks[i]);
                        break;
                }
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
    }
}
