package com.emn.trustydrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DocumentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_details);
        DocumentMetadata docMetadata = (DocumentMetadata) getIntent().getSerializableExtra("DOCUMENT_METADATA");

        TextView nameTextView = (TextView) findViewById(R.id.documentName);
        TextView sizeTextView = (TextView) findViewById(R.id.documentSize);
        TextView dateTextView = (TextView) findViewById(R.id.documentDate);

        nameTextView.setText(docMetadata.getFileName());
        sizeTextView.setText(docMetadata.displaySize());
        dateTextView.setText(docMetadata.displayDate());
    }
}
