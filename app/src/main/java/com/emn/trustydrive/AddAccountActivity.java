package com.emn.trustydrive;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.Provider;
import com.emn.trustydrive.tasks.GetEmailTask;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AddAccountActivity extends AppCompatActivity {
    private boolean authDropboxLaunched;
    private ProgressDialog progress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        authDropboxLaunched = false;
    }

    protected void onResume() {
        super.onResume();
        if (authDropboxLaunched) {
            String token = Auth.getOAuth2Token();
            if (token != null) {
                Account account = new Account(token, "", Provider.DROPBOX);
                this.showLoading();
                new GetEmailTask(account, new GetEmailTask.Callback() {
                    public void onTaskComplete(Account account) {
                        ArrayList<Account> accounts = DataHolder.getInstance().getAccounts();
                        accounts.add(account);
                        getSharedPreferences("trustyDrive", MODE_PRIVATE).edit()
                                .putString("accounts", new Gson().toJson(accounts)).apply();
                        DataHolder.getInstance().setAccounts(accounts);
                        progress.dismiss();
                        finish();
                    }

                    public void onError(Exception e) {
                        e.printStackTrace(); //TODO
                        progress.dismiss();
                    }
                }).execute();
            } else
                Toast.makeText(AddAccountActivity.this, "Access refused", Toast.LENGTH_LONG).show();
        }
    }

    public void showLoading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    public void addDropboxAccount(View v) {
        Auth.startOAuth2Authentication(AddAccountActivity.this, getString(R.string.app_key));
        authDropboxLaunched = true;
    }
}
