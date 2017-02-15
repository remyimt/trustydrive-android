package com.emn.trustydrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emn.trustydrive.adapters.AccountAdapter;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.TrustyDrive;
import com.emn.trustydrive.tasks.LoginTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private ArrayList<Account> accounts = new ArrayList<>();
    private ProgressDialog progress;
    private AccountAdapter accountAdapter;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView noAccountRegisteredTextView;
    private TextView warningTextView;
    private ListView accountsListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        noAccountRegisteredTextView = (TextView) findViewById(R.id.noAccountRegisteredTextView);
        warningTextView = (TextView) findViewById(R.id.warningTextView);
        accountsListView = (ListView) findViewById(R.id.accountsListView);
        loginButton = (Button) findViewById(R.id.loginButton);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if (accounts.size() > 1 && s.length() > 0) {
                    loginButton.setClickable(true);
                    loginButton.setAlpha(1f);
                } else {
                    loginButton.setClickable(false);
                    loginButton.setAlpha(.5f);
                }
            }
        });
//        getSharedPreferences("trustyDrive", MODE_PRIVATE).edit().remove("accounts").apply();
        accounts = new Gson().fromJson(getSharedPreferences("trustyDrive", MODE_PRIVATE)
                .getString("accounts", "[]"), new TypeToken<ArrayList<Account>>() {}.getType());
        DataHolder.getInstance().setAccounts(accounts);
        accountAdapter = new AccountAdapter(this, accounts);
        accountsListView.setAdapter(accountAdapter);
    }

    protected void onResume() {
        super.onResume();
        accounts = DataHolder.getInstance().getAccounts();
        accountAdapter.setAccounts(accounts);
        accountAdapter.notifyDataSetChanged();
        passwordEditText.setText("");
        loginButton.setClickable(false);
        loginButton.setAlpha(.5f);
        if (accounts.size() > 0) noAccountRegisteredTextView.setVisibility(View.GONE);
        if (accounts.size() >= 2) ((TextView) findViewById(R.id.warningTextView)).setText("");
    }

    public void login(View v) {
        final String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        this.showLoading();
        new LoginTask(password, this, new LoginTask.Callback() {
            public void onTaskComplete(TrustyDrive metadata) {
                DataHolder.getInstance().setMetadata(metadata);
                progress.dismiss();
                startActivity(new Intent(LoginActivity.this, FileListActivity.class));
                finish();
            }

            public void onError(List<Exception> exceptions) {
                for (Exception exception : exceptions) exception.printStackTrace(); //TODO
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Error or wrong password", Toast.LENGTH_SHORT).show();
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
}
