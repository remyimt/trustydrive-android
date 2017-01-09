package com.emn.trustydrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class MainActivity extends AppCompatActivity {
    private static final String ACCESS_TOKEN = "7U7cWDNwdEQAAAAAAAADU96AWKj6vpcitLAjzTr1lsCwXtK_zsxjw3W5ggbN5kDZ";
    private DbxClientV2 client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        this.client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    /**
     * Called when the user clicks the " Access documents " button
     */
    public void accessDocuments(View v) {
        Intent intent = new Intent(this, AccessDocumentsActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the " Manage registered accounts " button
     */
    public void manageAccounts(View v) {
        Intent intent = new Intent(this, ManageCloudAccounts.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the " Add cloud accounts  " button
     */
    public void addCloudAccounts(View v) {
        Intent intent = new Intent(this, AddCloudAccount.class);
        startActivity(intent);
    }
}
