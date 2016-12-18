package com.emn.trustydrive;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

class GetEmail extends AsyncTask<DbxClientV2, Void, String> {

    protected String doInBackground(DbxClientV2... clients) {
        DbxClientV2 client = clients[0];
        String email = "";
        try {
            email = client.users().getCurrentAccount().getEmail();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return email;
    }

    protected void onPostExecute(String email) {
        Log.i("email", email);
    }
}