package com.emn.trustydrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ManageCloudAccounts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cloud_accounts);

        String[] fakeAccounts = {"dropbox 1", "dropbox 2", "google drive 1"};
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fakeAccounts);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }
}
