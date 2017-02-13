package com.emn.trustydrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emn.trustydrive.adapters.AccountAdapter;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.emn.trustydrive.tasks.LoginTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private ArrayList<Account> accounts = new ArrayList<>();
    private ProgressDialog progress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSharedPreferences("trustyDrive", MODE_PRIVATE).edit().remove("accounts").apply();
    }

    protected void onResume() {
        super.onResume();
        accounts = new Gson().fromJson(getSharedPreferences("trustyDrive", MODE_PRIVATE)
                .getString("accounts", "[]"), new TypeToken<ArrayList<Account>>() {}.getType());
        ArrayList<String> emails = new ArrayList<>();
        for (Account account : accounts) emails.add(account.getEmail());
//        ((ListView) findViewById(R.id.accountsListView)).setAdapter(
//                new ArrayAdapter(LoginActivity.this, android.R.layout.simple_list_item_1, emails));
        ((ListView) findViewById(R.id.accountsListView)).setAdapter(new AccountAdapter(this, accounts, true));
        checkAtLeastOneAccount();
        warnIfNotEnoughAccounts();
    }

    private void checkAtLeastOneAccount() {
        TextView noAccountRegisteredTextView = (TextView) findViewById(R.id.noAccountRegisteredTextView);
        if (accounts.size() == 0) noAccountRegisteredTextView.setText(R.string.noAccountRegistered);
        else noAccountRegisteredTextView.setText(R.string.registeredAccounts);
    }

    private void warnIfNotEnoughAccounts() {
        TextView warningTextView = (TextView) findViewById(R.id.warningTextView);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        if (accounts.size() < 2) {
            warningTextView.setText(R.string.notEnoughAccountsWarning);
            loginButton.setClickable(false);
            loginButton.setAlpha(.5f);
        } else {
            warningTextView.setText("");
            loginButton.setClickable(true);
            loginButton.setAlpha(1.0f);
        }
    }

    public void login(View v) {
        final String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        this.showLoading();
        new LoginTask(accounts, password, this, new LoginTask.Callback() {
            public void onTaskComplete(TrustyDrive metadata) {
                for (Account account : accounts)
                    account.setMetadataFileName(account.createHash(password));
                startActivity(new Intent(LoginActivity.this, FileListActivity.class)
                        .putExtra("metadata", metadata)
                        .putParcelableArrayListExtra("accounts", accounts));
                progress.dismiss();
                finish();
            }

            public void onError(List<Exception> exceptions) {
                Toast.makeText(LoginActivity.this, "Error or wrong password", Toast.LENGTH_SHORT).show();
                for (Exception exception : exceptions) exception.printStackTrace(); //TODO
                progress.dismiss();
            }
        }).execute();
    }

    public void addCloudAccount(View v) {
        startActivity(new Intent(this, AddAccountActivity.class));
    }

    public void showLoading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    public void launchRegisterActivity(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void launchTestFoldersActivity(View view) {
        startActivity(new Intent(this, TestFoldersActivity.class));
    }
}
