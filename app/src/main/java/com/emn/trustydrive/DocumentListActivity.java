package com.emn.trustydrive;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;

public class DocumentListActivity extends AppCompatActivity {
    public static ArrayList<DocumentMetadata> fakeDocuments = new ArrayList<>();
    private DocumentAdapter documentAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_documents);
        fakeDocuments = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            DocumentMetadata doc = new DocumentMetadata("Document " + i, new Date(117, 0, i), 10, i);
            fakeDocuments.add(doc);
        }
        documentAdapter = new DocumentAdapter(DocumentListActivity.this, fakeDocuments);
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
                Toast.makeText(this, "ToDo", Toast.LENGTH_LONG).show(); //ToDo : upload file
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteFile(int position) {
        documentAdapter.deleteDocument(position);
        documentAdapter.notifyDataSetChanged();
        Toast.makeText(DocumentListActivity.this, "File deleted", Toast.LENGTH_SHORT).show();
    }

    public void displayFileOptions(View view) {
        FileOptionsDialogFragment dialog = new FileOptionsDialogFragment();
        dialog.setFilePosition((int) view.getTag());
        dialog.show(getFragmentManager(), null);
    }

    public void toggleSavedOnDeviceStatus(int filePosition) {
        //TODO: stocker ou supprimer le fichier sur le tel pour de vrai
        DocumentMetadata doc = documentAdapter.getDocs().get(filePosition);
        doc.toggleSavedOnDeviceStatus();
        documentAdapter.notifyDataSetChanged();
        String message = doc.isSavedOnDevice() ? "File saved on device" : "File deleted from device";
        Toast.makeText(DocumentListActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void openFile(View view) {
        //TODO: ouvrir le fichier
        DocumentMetadata doc = documentAdapter.getDocs().get((int) view.getTag());
        Toast.makeText(DocumentListActivity.this, "Todo: Open file", Toast.LENGTH_SHORT).show();
    }

    public void addFileOrFolder(View view) {
        AddFileOrFolderDialogFragment dialog = new AddFileOrFolderDialogFragment();
        dialog.show(getFragmentManager(), null);
    }
}
