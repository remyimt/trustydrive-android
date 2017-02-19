package com.emn.trustydrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.emn.trustydrive.adapters.AccountAdapter;
import com.emn.trustydrive.metadata.DataHolder;

public class SettingsActivity extends AppCompatActivity {
    private AccountAdapter accountAdapter;
    private int initialCount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        accountAdapter = new AccountAdapter(this, DataHolder.getInstance().getAccounts());
        initialCount = accountAdapter.getCount();
        ((ListView) findViewById(R.id.accountsListView)).setAdapter(accountAdapter);
    }

    protected void onResume() {
        super.onResume();
        if (accountAdapter.getCount() > initialCount) {
            Toast.makeText(SettingsActivity.this, "Log again to finish the add", Toast.LENGTH_LONG).show();
            logout();
        }
    }

    public void logout(View view) {
        logout();
    }

    public void logout() {
        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public void addAccount(View view) {
        startActivity(new Intent(this, AddAccountActivity.class));
    }
}
