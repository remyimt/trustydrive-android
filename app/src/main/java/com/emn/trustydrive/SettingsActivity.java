package com.emn.trustydrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.emn.trustydrive.providers.CloudAccount;
import com.emn.trustydrive.providers.dropbox.DropboxAccount;
import com.emn.trustydrive.providers.google_drive.GoogleDriveAccount;
import com.emn.trustydrive.providers.onedrive.OneDriveAccount;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private ArrayList<CloudAccount> fakeCloudAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFakeCloudAccounts();
        setContentView(R.layout.activity_settings);
        ListView listView = (ListView) findViewById(R.id.registeredAccountsListView);
        listView.setAdapter(new CloudAdapter(this, createFakeCloudAccounts()));
    }

    private ArrayList<CloudAccount> createFakeCloudAccounts() {
        ArrayList<CloudAccount> fakeCloudAccounts = new ArrayList<>();
        fakeCloudAccounts.add(new DropboxAccount("fakeToken", "fake1@gmail.com"));
        fakeCloudAccounts.add(new GoogleDriveAccount("fakeToken", "fake2@gmail.com"));
        fakeCloudAccounts.add(new OneDriveAccount("fakeToken", "fake3@gmail.com"));
        return fakeCloudAccounts;
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        // set the new task and clear flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
