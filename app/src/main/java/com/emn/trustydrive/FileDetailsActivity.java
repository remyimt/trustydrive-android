package com.emn.trustydrive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.emn.trustydrive.adapters.AccountAdapter;
import com.emn.trustydrive.metadata.Account;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.FileData;

import java.util.ArrayList;
import java.util.List;

public class FileDetailsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);
        FileData fileData = DataHolder.getInstance().getFile();
        ((TextView) findViewById(R.id.documentName)).setText(fileData.getName());
        ((TextView) findViewById(R.id.documentSize)).setText(fileData.displaySize());
        ((TextView) findViewById(R.id.documentDate)).setText(fileData.displayDate());

        List<Account> accounts = new ArrayList<>();
        for (ChunkData chunkData: fileData.getChunks()) {
            if (!accounts.contains(chunkData.getAccount())) {
                accounts.add(chunkData.getAccount());
            }
        }

        ListView cloudAccountsListView = ((ListView) findViewById(R.id.cloudAccountsListView));
        cloudAccountsListView.setAdapter(new AccountAdapter(this, accounts));
    }
}
