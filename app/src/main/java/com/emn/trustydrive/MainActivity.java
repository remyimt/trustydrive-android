package com.emn.trustydrive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.emn.trustydrive.providers.CloudAccount;
import com.emn.trustydrive.providers.CloudAccountData;
import com.emn.trustydrive.providers.dropbox.DropboxAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.emn.trustydrive.providers.ICloudAccount.DROPBOX;

public class MainActivity extends AppCompatActivity {
    private ArrayList<CloudAccount> cloudAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ArrayList<String> emails = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("trustyDrive", MODE_PRIVATE);
        ArrayList<CloudAccountData> accountsData = new Gson().fromJson(prefs.getString("accounts", "[]"),
                new TypeToken<ArrayList<CloudAccountData>>() {}.getType());
        for (CloudAccountData accountData : accountsData)
            switch (accountData.getProvider()) {
                case DROPBOX:
                    cloudAccounts.add(new DropboxAccount(accountData.getToken(), accountData.getEmail()));
                    emails.add(accountData.getEmail());
                    break;
            }
        ((ListView) findViewById(R.id.accountsListView)).setAdapter(
                new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, emails));
    }

    public void login(View v) {
        String passwordEntered = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        //ToDo: check password
        Intent intent = new Intent(this, DocumentListActivity.class);
        startActivity(intent);
    }

    public void addCloudAccount(View v) {
        Intent intent = new Intent(this, AddCloudAccount.class);
        startActivity(intent);
    }
}
