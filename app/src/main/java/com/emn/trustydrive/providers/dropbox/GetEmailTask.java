package com.emn.trustydrive.providers.dropbox;

import android.os.AsyncTask;

import com.dropbox.core.v2.DbxClientV2;

public class GetEmailTask extends AsyncTask<Object, Void, String> {
    private final DbxClientV2 client;
    private final Callback mCallback;
    private Exception mException;

    public interface Callback {
        void onTaskComplete(String email);

        void onError(Exception e);
    }

    public GetEmailTask(DbxClientV2 dbxClient, Callback callback) {
        client = dbxClient;
        mCallback = callback;
    }

    protected String doInBackground(Object... objects) {
        try {
            return client.users().getCurrentAccount().getEmail();
        } catch (Exception e) {
            mException = e;
            return null;
        }
    }

    protected void onPostExecute(String email) {
        super.onPostExecute(email);
        if (mException != null) mCallback.onError(mException);
        else if (email == null) mCallback.onError(null);
        else mCallback.onTaskComplete(email);
    }
}
