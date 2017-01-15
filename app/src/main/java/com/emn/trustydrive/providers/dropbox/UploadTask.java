package com.emn.trustydrive.providers.dropbox;

import android.os.AsyncTask;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;

import java.io.FileInputStream;

class UploadTask extends AsyncTask<Object, Void, Metadata> {
    private final DbxClientV2 client;
    private final Callback mCallback;
    private Exception mException;

    interface Callback {
        void onTaskComplete(FileMetadata result);

        void onError(Exception e);
    }

    UploadTask(DbxClientV2 dbxClient, Callback callback) {
        client = dbxClient;
        mCallback = callback;
    }

    protected Metadata doInBackground(Object... objects) {
        FileInputStream in = (FileInputStream) objects[0];
        String remoteName = (String) objects[1];
        try {
            return client.files().uploadBuilder("/" + remoteName).uploadAndFinish(in);
        } catch (Exception e) {
            mException = e;
            return null;
        }
    }

    protected void onPostExecute(FileMetadata result) {
        super.onPostExecute(result);
        if (mException != null) mCallback.onError(mException);
        else if (result == null) mCallback.onError(null);
        else mCallback.onTaskComplete(result);
    }
}
