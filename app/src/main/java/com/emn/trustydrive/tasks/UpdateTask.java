package com.emn.trustydrive.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.DataHolder;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UpdateTask extends AsyncTask<Object, Void, Void> {
    private Activity activity;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete();

        void onError(List<Exception> exceptions);
    }

    public UpdateTask(Activity activity, Callback callback) {
        this.activity = activity;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected Void doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start");
        List<ChunkData> chunksData = new ArrayList<>();
        for (Account account : DataHolder.getInstance().getAccounts())
            chunksData.add(new ChunkData(account, account.getMetadataFileName()));
        InputStream inputStream = new ByteArrayInputStream(new Gson().toJson(DataHolder.getInstance().getMetadata()).getBytes(StandardCharsets.UTF_8));
        int size = chunksData.size();
        int steps = 0;
        FileInputStream[] chunks = new FileInputStream[size];
        try {
            FileOutputStream[] chunksOut = new FileOutputStream[size];
            for (int i = 0; i < size; i++)
                chunksOut[i] = activity.openFileOutput(chunksData.get(i).getName(), Context.MODE_PRIVATE);
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
            for (FileOutputStream chunk : chunksOut) chunk.close();
            for (int j = 0; j < size; j++)
                chunks[j] = activity.openFileInput(chunksData.get(j).getName());
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
                                + chunkData.getName()).withMode(WriteMode.OVERWRITE)
                                .uploadAndFinish(chunks[i]);
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
        else callback.onTaskComplete();
    }
}
