package com.emn.trustydrive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emn.trustydrive.providers.CloudAccount;
import com.emn.trustydrive.providers.CloudAccountData;
import com.emn.trustydrive.providers.dropbox.DropboxAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.emn.trustydrive.providers.ICloudAccount.DROPBOX;

public class MainActivity extends AppCompatActivity {
    private ArrayList<CloudAccount> cloudAccounts = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private TextView warningTextView;
    private Button loginButton;
    private TextView noAccountRegisteredTextView;
    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.content_main);
        warningTextView = (TextView) findViewById(R.id.warningTextView);
        loginButton = (Button) findViewById(R.id.loginButton);
        noAccountRegisteredTextView = (TextView) findViewById(R.id.noAccountRegisteredTextView);
        passwordConfirmationEditText = (EditText) findViewById(R.id.passwordConfirmationEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        sharedPreferences = getSharedPreferences("trustyDrive", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> emails = new ArrayList<>();
//        sharedPreferences.edit().putString("accounts", null).apply(); // Use to reset log accounts
        ArrayList<CloudAccountData> accountsData = new Gson().fromJson(sharedPreferences.getString("accounts", "[]"),
                new TypeToken<ArrayList<CloudAccountData>>() {}.getType());
        for (CloudAccountData accountData : accountsData)
            switch (accountData.getProvider()) {
                case DROPBOX:
                    cloudAccounts.add(new DropboxAccount(accountData.getToken(), accountData.getEmail()));
                    emails.add(accountData.getEmail());
                    break;
            }
        ((ListView) findViewById(R.id.accountsListView)).setAdapter(
                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, emails));

        checkAlreadyRegistered();
        checkAtLeastOneAccount();
        warnIfNotEnoughAccounts();
    }

    private void checkAlreadyRegistered() {
        if (sharedPreferences.getString("password", null) == null) {
            loginButton.setText(R.string.register);
            passwordConfirmationEditText.setVisibility(View.VISIBLE);
        } else {
            loginButton.setText(R.string.login);
            passwordConfirmationEditText.setVisibility(View.GONE);
        }
    }

    private void checkAtLeastOneAccount() {
        if (cloudAccounts.size() == 0) {
            noAccountRegisteredTextView.setText(R.string.noAccountRegistered);
        } else {
            noAccountRegisteredTextView.setText(R.string.registeredAccounts);
        }
    }

    private void warnIfNotEnoughAccounts() {
        if (cloudAccounts.size() < 2) {
            warningTextView.setText(R.string.notEnoughAccountsWarning);
            loginButton.setClickable(false);
            loginButton.setAlpha(.5f);
        } else {
            warningTextView.setText(R.string.empty);
            loginButton.setClickable(true);
            loginButton.setAlpha(1.0f);
        }
    }

    public void loginOrRegister(View v) {
        String passwordEntered = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String passwordConfirmationEntered = ((EditText) findViewById(R.id.passwordConfirmationEditText)).getText().toString();
        String savedPassword = sharedPreferences.getString("password", null);
        if ( savedPassword == null) {
            if (passwordConfirmationEntered.equals(passwordEntered)) {
                sharedPreferences.edit().putString("password", passwordEntered).apply();
                passwordEditText.setText("");
                Intent intent = new Intent(this, DocumentListActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            }
        } else {
            if (passwordEntered.equals(savedPassword)) {
                passwordEditText.setText("");
                Intent intent = new Intent(this, DocumentListActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Password incorrect", Toast.LENGTH_SHORT).show();
                passwordEditText.setText("");
            }
        }
    }

    public void addCloudAccount(View v) {
        Intent intent = new Intent(this, AddCloudAccount.class);
        startActivity(intent);
    }
}
