package com.emn.trustydrive.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.DataHolder;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UpdateTask extends AsyncTask<Object, Void, Void> {
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete();

        void onError(List<Exception> exceptions);
    }

    public UpdateTask(Callback callback) {
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected Void doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start");
        List<Account> accounts = DataHolder.getInstance().getAccounts();
        InputStream inputStream = new ByteArrayInputStream(new Gson().toJson(DataHolder.getInstance().getMetadata()).getBytes(StandardCharsets.UTF_8));
        int size = accounts.size();
        InputStream[] chunks = new InputStream[size];
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
            }
            for (int i = 0; i < size; i++)
                chunks[i] = new ByteArrayInputStream(chunksOut[i].toByteArray());
        } catch (IOException e) {
            exceptions.add(e);
        }
        Log.i(this.getClass().getSimpleName(), "Break metadata finished");
        for (int i = 0; i < size; i++) {
            try {
                switch (accounts.get(i).getProvider()) {
                    case DROPBOX:
                        new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                accounts.get(i).getToken()).files().uploadBuilder("/"
                                + accounts.get(i).getMetadataFileName()).withMode(WriteMode.OVERWRITE)
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
