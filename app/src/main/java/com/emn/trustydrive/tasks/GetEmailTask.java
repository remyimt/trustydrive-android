package com.emn.trustydrive.tasks;

import android.os.AsyncTask;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.metadata.Account;

public class GetEmailTask extends AsyncTask<Object, Void, String> {
    private Account account;
    private Callback callback;
    private Exception exception;

    public interface Callback {
        void onTaskComplete(Account account);

        void onError(Exception e);
    }

    public GetEmailTask(Account account, Callback callback) {
        this.account = account;
        this.callback = callback;
    }

    protected String doInBackground(Object... objects) {
        try {
            switch (account.getProvider()) {
                case DROPBOX:
                    return new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                            account.getToken()).users().getCurrentAccount().getEmail();
            }
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    protected void onPostExecute(String email) {
        if (exception != null || email == null) callback.onError(exception);
        else {
            account.setEmail(email);
            callback.onTaskComplete(account);
        }
    }
}
