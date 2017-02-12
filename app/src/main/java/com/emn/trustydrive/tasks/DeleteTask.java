package com.emn.trustydrive.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.TrustyDrive;

import java.util.ArrayList;
import java.util.List;

public class DeleteTask extends AsyncTask<Object, Void, String> {
    private List<ChunkData> chunksData;
    private TrustyDrive metadata;
    private List<Account> accounts;
    private Activity activity;
    private Callback callback;
    private List<Exception> exceptions;

    public interface Callback {
        void onTaskComplete();

        void onError(List<Exception> exceptions);
    }

    public DeleteTask(List<ChunkData> chunksData, TrustyDrive metadata, List<Account> accounts, Activity activity, Callback callback) {
        this.chunksData = chunksData;
        this.metadata = metadata;
        this.accounts = accounts;
        this.activity = activity;
        this.callback = callback;
        this.exceptions = new ArrayList<>();
    }

    protected String doInBackground(Object... objects) {
        Log.i(this.getClass().getSimpleName(), "Start delete");
        for (ChunkData chunkData : chunksData)
            try {
                switch (chunkData.getAccount().getProvider()) {
                    case DROPBOX:
                        new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                                chunkData.getAccount().getToken()).files().delete("/" + chunkData.getName());
                }
            } catch (Exception e) {
                exceptions.add(e);
            }
        return null;
    }

    protected void onPostExecute(String email) {
        if (exceptions.size() > 0) callback.onError(exceptions);
        else {
            Log.i(this.getClass().getSimpleName(), "Chunks deleted");
            new UploadTask(null, null, metadata, accounts, activity, new UploadTask.Callback() {
                public void onTaskComplete() {
                    callback.onTaskComplete();
                }

                public void onError(List<Exception> exceptions) {
                    callback.onError(exceptions);
                }
            }).execute();
        }
    }
}
