package com.emn.trustydrive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.emn.trustydrive.metadata.DataHolder;
import com.emn.trustydrive.metadata.FileData;

public class FileDetailsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);
        FileData fileData = DataHolder.getInstance().getFile();
        ((TextView) findViewById(R.id.documentName)).setText(fileData.getName());
        ((TextView) findViewById(R.id.documentSize)).setText(fileData.displaySize());
        ((TextView) findViewById(R.id.documentDate)).setText(fileData.displayDate());
    }
}
