package com.emn.trustydrive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.providers.CloudAccountData;
import com.emn.trustydrive.providers.ICloudAccount;
import com.emn.trustydrive.providers.dropbox.GetEmailTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AddCloudAccount extends AppCompatActivity {
    private boolean authDropboxLaunched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cloud_account);
        authDropboxLaunched = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authDropboxLaunched) {
            final String token = Auth.getOAuth2Token();
            if (token != null) {
                Toast.makeText(AddCloudAccount.this, "Please wait", Toast.LENGTH_LONG).show(); //Todo: display loading logo and disable view
                new GetEmailTask(new DbxClientV2(DbxRequestConfig.newBuilder("trustyDrive").build(),
                        token), new GetEmailTask.Callback() {
                    public void onTaskComplete(String email) {
                        SharedPreferences prefs = getSharedPreferences("trustyDrive", MODE_PRIVATE);
                        Gson gson = new Gson();
                        ArrayList<CloudAccountData> accounts = gson.fromJson(prefs.getString("accounts", "[]"),
                                new TypeToken<ArrayList<CloudAccountData>>() {}.getType());
                        CloudAccountData newAccount = new CloudAccountData(token, ICloudAccount.DROPBOX, email);
                        accounts.add(newAccount);
                        prefs.edit().putString("accounts", gson.toJson(accounts)).apply();
                        finish();
                    }
                    public void onError(Exception e) {
                        e.printStackTrace(); //ToDo: Display error message
                    }
                }).execute();
            } else
                Toast.makeText(AddCloudAccount.this, "Access refused", Toast.LENGTH_LONG).show();
        }
    }

    public void addDropboxAccount(View v) {
        Auth.startOAuth2Authentication(AddCloudAccount.this, getString(R.string.app_key));
        authDropboxLaunched = true;
    }
}
