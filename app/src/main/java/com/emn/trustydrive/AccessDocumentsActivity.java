package com.emn.trustydrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AccessDocumentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_documents);

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
        intent.setType("file/*");
        startActivity(intent);
    }
}
