package com.emn.trustydrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.emn.trustydrive.providers.CloudAccount;
import com.emn.trustydrive.providers.DropboxAccount;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String ACCESS_TOKEN = "7U7cWDNwdEQAAAAAAAADU96AWKj6vpcitLAjzTr1lsCwXtK_zsxjw3W5ggbN5kDZ";
    private DbxClientV2 client;
    private List<CloudAccount> cloudAccounts;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        setCloudAccounts();
        ListView accountsListView = (ListView) findViewById(R.id.accountsListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cloudAccountEmails());
        accountsListView.setAdapter(arrayAdapter);
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        this.client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    private ArrayList<String> cloudAccountEmails() {
        ArrayList result = new ArrayList();
        for (CloudAccount account: cloudAccounts) {
            result.add(account.getEmail());
        }
        return result;
    }

    private void setCloudAccounts() {
        //Todo: récupérer les vrais comptes à la place de ceux-là qui sont fictifs
        cloudAccounts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            cloudAccounts.add(new DropboxAccount("dropbox" + i + "@gmail.com"));
        }
    }

    public void login(View v) {
        String passwordEntered = passwordEditText.getText().toString();
        if (passwordIsValid(passwordEntered)) {
            accessDocuments();
        }
    }

    private boolean passwordIsValid(String passwordEntered) {
        //TODO
        return true;
    }

    public void accessDocuments() {
        Intent intent = new Intent(this, DocumentListActivity.class);
        startActivity(intent);
    }


    /**
     * Called when the user clicks the " Manage registered accounts " button
     */
    public void manageAccounts(View v) {
        Intent intent = new Intent(this, ManageCloudAccounts.class);
        startActivity(intent);
    }

    public void addCloudAccount(View v) {
        Intent intent = new Intent(this, AddCloudAccount.class);
        startActivity(intent);
    }

}
