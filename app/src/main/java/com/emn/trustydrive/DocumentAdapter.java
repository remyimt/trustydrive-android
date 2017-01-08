package com.emn.trustydrive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DocumentAdapter extends BaseAdapter {

    private final List<DocumentMetadata> docs;
    private final LayoutInflater inflater;

    public DocumentAdapter(Context context, List<DocumentMetadata> docs) {
        this.docs = docs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return docs.size();
    }

    @Override
    public DocumentMetadata getItem(int position) {
        return docs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return docs.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DocumentMetadataViewHolder bookViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_item_document, parent, false);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            Button detailsButton = (Button) convertView.findViewById(R.id.detailsButton);
            Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
            deleteButton.setTag(position);
            detailsButton.setTag(position);
            convertView.setTag(new DocumentMetadataViewHolder(nameTextView, detailsButton, deleteButton));
        }
        DocumentMetadata doc = docs.get(position);
        bookViewHolder = (DocumentMetadataViewHolder) convertView.getTag();
        bookViewHolder.nameTextView.setText(doc.getFileName());
        bookViewHolder.deleteButton.setTag(position);
        bookViewHolder.detailsButton.setTag(position);
        return convertView;
    }

    private static class DocumentMetadataViewHolder {
        final TextView nameTextView;
        final TextView detailsButton;
        final Button deleteButton;

        private DocumentMetadataViewHolder(TextView nameTextView, Button detailsButton, Button deleteButton) {
            this.nameTextView = nameTextView;
            this.detailsButton = detailsButton;
            this.deleteButton = deleteButton;
        }
    }

    public void deleteDocument(int position) {
        docs.remove(position);
        Log.i("Lol", "" + position);
    }

    public DocumentMetadata getDocumentMetadata(int position) {
        return docs.get(position);
    }

}