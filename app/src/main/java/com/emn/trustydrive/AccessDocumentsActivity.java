package com.emn.trustydrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.FileInputStream;

public class AccessDocumentsActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN = "7U7cWDNwdEQAAAAAAAADU96AWKj6vpcitLAjzTr1lsCwXtK_zsxjw3W5ggbN5kDZ";
    private DbxClientV2 client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_documents);

        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        this.client = new DbxClientV2(config, ACCESS_TOKEN);

        String[] fakeDocuments = new String[20];
        for (int i=0; i<20; i++) {
            fakeDocuments[i] = "fake document " + i;
        }
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fakeDocuments);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    /**
     * Called when the user clicks the " Add document " button
     */
    public void chooseFile(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivity(intent);
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            try {
                FileInputStream in = (FileInputStream) getContentResolver().openInputStream(data.getData());
                new UploadFile().execute(client, in);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
