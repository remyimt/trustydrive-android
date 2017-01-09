package com.emn.trustydrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dropbox.core.android.Auth;

public class AddCloudAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cloud_account);
    }

    public void addDropboxAccount(View v) {
        Auth.startOAuth2Authentication(AddCloudAccount.this, getString(R.string.app_key));
    }
}
