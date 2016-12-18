package com.emn.trustydrive;

import android.os.AsyncTask;

import com.dropbox.core.v2.DbxClientV2;

import java.io.FileInputStream;

class UploadFile extends AsyncTask<Object, Void, String> {

    protected String doInBackground(Object... objects) {
        DbxClientV2 client = (DbxClientV2) objects[0];
        FileInputStream in = (FileInputStream) objects[1];
        try {
            client.files().uploadBuilder("/test").uploadAndFinish(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}