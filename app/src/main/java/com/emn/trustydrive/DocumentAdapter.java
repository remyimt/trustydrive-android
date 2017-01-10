package com.emn.trustydrive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        DocumentMetadataViewHolder documentMetadataViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_item_document, parent, false);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            TextView sizeAndDateTextView = (TextView) convertView.findViewById(R.id.sizeAndDateTextView);
            LinearLayout documentInfo = (LinearLayout) convertView.findViewById(R.id.documentInfo);
            ImageButton fileOptionsButton = (ImageButton) convertView.findViewById(R.id.fileOptionsButton);
            ImageView imageIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
            ImageView storedStatusImage = (ImageView) convertView.findViewById(R.id.storedStatusImage);
            convertView.setTag(new DocumentMetadataViewHolder(nameTextView, sizeAndDateTextView, documentInfo, fileOptionsButton, imageIcon, storedStatusImage));
        }
        DocumentMetadata doc = docs.get(position);
        documentMetadataViewHolder = (DocumentMetadataViewHolder) convertView.getTag();
        documentMetadataViewHolder.nameTextView.setText(doc.getFileName());
        documentMetadataViewHolder.sizeAndDateTextView.setText(doc.displaySize() + ", " + doc.displayDate());
        documentMetadataViewHolder.documentInfo.setTag(position);
        documentMetadataViewHolder.fileOptionsButton.setTag(position);
        int storedOnDeviceIcon = doc.isSavedOnDevice() ? R.mipmap.on_device_icon : 0;
        documentMetadataViewHolder.storedStatusImage.setImageResource(storedOnDeviceIcon);
        return convertView;
    }

    public List<DocumentMetadata> getDocs() {
        return docs;
    }

    private static class DocumentMetadataViewHolder {
        final TextView nameTextView;
        final TextView sizeAndDateTextView;
        final LinearLayout documentInfo;
        final ImageButton fileOptionsButton;
        final ImageView imageIcon;
        final ImageView storedStatusImage;

        private DocumentMetadataViewHolder(TextView nameTextView, TextView sizeAndDateTextView, LinearLayout documentInfo, ImageButton fileOptionsButton, ImageView imageIcon, ImageView storedStatusImage) {
            this.nameTextView = nameTextView;
            this.sizeAndDateTextView = sizeAndDateTextView;
            this.documentInfo = documentInfo;
            this.fileOptionsButton = fileOptionsButton;
            this.imageIcon = imageIcon;
            this.storedStatusImage = storedStatusImage;
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