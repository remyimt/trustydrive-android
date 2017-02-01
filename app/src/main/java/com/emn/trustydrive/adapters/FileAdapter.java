package com.emn.trustydrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emn.trustydrive.R;
import com.emn.trustydrive.metadata.FileData;
import com.emn.trustydrive.metadata.TrustyDrive;

import java.util.List;

public class FileAdapter extends BaseAdapter {
    private final TrustyDrive metadata;
    private final LayoutInflater inflater;

    public FileAdapter(Context context, TrustyDrive metadata) {
        this.metadata = metadata;
        inflater = LayoutInflater.from(context);
    }

    public List<FileData> getFilesData() {
        return metadata.getFilesData();
    }

    public void deleteFile(int position) {
        metadata.getFilesData().remove(position);
    }

    public void add(FileData fileData) {
        metadata.getFilesData().add(fileData);
    }

    @Override
    public int getCount() {
        return metadata.getFilesData().size();
    }

    @Override
    public FileData getItem(int position) {
        return metadata.getFilesData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileViewHolder fileViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_item_document, parent, false);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            TextView sizeAndDateTextView = (TextView) convertView.findViewById(R.id.sizeAndDateTextView);
            LinearLayout documentInfo = (LinearLayout) convertView.findViewById(R.id.documentInfo);
            ImageButton fileOptionsButton = (ImageButton) convertView.findViewById(R.id.fileOptionsButton);
            ImageView imageIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
            ImageView storedStatusImage = (ImageView) convertView.findViewById(R.id.storedStatusImage);
            convertView.setTag(new FileViewHolder(nameTextView, sizeAndDateTextView, documentInfo, fileOptionsButton, imageIcon, storedStatusImage));
        }
        FileData fileData = metadata.getFilesData().get(position);
        fileViewHolder = (FileViewHolder) convertView.getTag();
        fileViewHolder.nameTextView.setText(fileData.getName());
        fileViewHolder.sizeAndDateTextView.setText(fileData.displaySize() + ", " + fileData.displayDate());
        fileViewHolder.documentInfo.setTag(position);
        fileViewHolder.fileOptionsButton.setTag(position);
        int storedOnDeviceIcon = false ? R.mipmap.on_device_icon : 0; //TODO
        fileViewHolder.storedStatusImage.setImageResource(storedOnDeviceIcon);
        return convertView;
    }

    private static class FileViewHolder {
        final TextView nameTextView;
        final TextView sizeAndDateTextView;
        final LinearLayout documentInfo;
        final ImageButton fileOptionsButton;
        final ImageView imageIcon;
        final ImageView storedStatusImage;

        private FileViewHolder(TextView nameTextView, TextView sizeAndDateTextView, LinearLayout documentInfo, ImageButton fileOptionsButton, ImageView imageIcon, ImageView storedStatusImage) {
            this.nameTextView = nameTextView;
            this.sizeAndDateTextView = sizeAndDateTextView;
            this.documentInfo = documentInfo;
            this.fileOptionsButton = fileOptionsButton;
            this.imageIcon = imageIcon;
            this.storedStatusImage = storedStatusImage;
        }
    }
}