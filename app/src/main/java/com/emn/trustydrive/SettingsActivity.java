package com.emn.trustydrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.emn.trustydrive.adapters.AccountAdapter;
import com.emn.trustydrive.metadata.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    protected void onResume() {
        super.onResume();
        List<Account> accounts = new Gson().fromJson(getSharedPreferences("trustyDrive", MODE_PRIVATE)
                .getString("accounts", "[]"), new TypeToken<ArrayList<Account>>() {}.getType());
        ((ListView) findViewById(R.id.registeredAccountsListView)).setAdapter(new AccountAdapter(this, accounts, false));
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void addAccount(View view) {
        startActivity(new Intent(this, AddAccountActivity.class));
    }
}
