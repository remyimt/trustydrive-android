package com.emn.trustydrive.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.AddAccountActivity;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.Provider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GetEmailTask extends AsyncTask<Object, Void, Account> {
    private String token;
    private Provider provider;
    private AddAccountActivity callingActivity;
    private Callback callback;
    private Exception exception;

    public interface Callback {
        void onTaskComplete();

        void onError(Exception e);
    }

    public GetEmailTask(String token, Provider provider, AddAccountActivity callingActivity, Callback callback) {
        this.token = token;
        this.provider = provider;
        this.callingActivity = callingActivity;
        this.callback = callback;
        callingActivity.showLoading();
    }

    protected Account doInBackground(Object... objects) {
        try {
            switch (provider) {
                case DROPBOX:
                    String email = new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                            token).users().getCurrentAccount().getEmail();
                    return new Account(token, email, Provider.DROPBOX);
            }
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    protected void onPostExecute(Account account) {
        if (exception != null || account == null) callback.onError(exception);
        else {
            SharedPreferences prefs = callingActivity.getSharedPreferences("trustyDrive", MODE_PRIVATE);
            ArrayList<Account> accounts = new Gson().fromJson(prefs.getString("accounts", "[]"),
                    new TypeToken<ArrayList<Account>>() {}.getType());
            accounts.add(account);
            prefs.edit().putString("accounts", new Gson().toJson(accounts)).apply();
            callback.onTaskComplete();
        }
        callingActivity.dismissLoading();
    }
}
