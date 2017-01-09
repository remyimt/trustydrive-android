package com.emn.trustydrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;

public class AccessDocumentsActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN = "7U7cWDNwdEQAAAAAAAADU96AWKj6vpcitLAjzTr1lsCwXtK_zsxjw3W5ggbN5kDZ";
    private DbxClientV2 client;
    private DocumentAdapter documentAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_documents);

        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        this.client = new DbxClientV2(config, ACCESS_TOKEN);

        ArrayList<DocumentMetadata> fakeDocuments = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            DocumentMetadata doc = new DocumentMetadata("Document " + i, new Date(117, 0, i), 10, i);
            fakeDocuments.add(doc);
        }
        documentAdapter = new DocumentAdapter(AccessDocumentsActivity.this, fakeDocuments);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(documentAdapter);
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

    public void deleteFile(View v) {
        int position = (int) v.getTag();
        documentAdapter.deleteDocument(position);
        documentAdapter.notifyDataSetChanged();
        Toast.makeText(AccessDocumentsActivity.this, "Document supprimé", Toast.LENGTH_SHORT).show();
    }

    public void displayDocumentDetails(View v) {
        Intent intent = new Intent(this, DocumentDetailsActivity.class);
        int position = (int) v.getTag();
        intent.putExtra("DOCUMENT_METADATA", documentAdapter.getDocumentMetadata(position));
        startActivity(intent);
    }

}
