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
import com.emn.trustydrive.metadata.Type;

import java.util.ArrayList;

public class FileAdapter extends BaseAdapter {
    private ArrayList<FileData> files;
    private final LayoutInflater inflater;

    public FileAdapter(Context context, ArrayList<FileData> files) {
        this.files = files;
        inflater = LayoutInflater.from(context);
    }

    public void setFiles(ArrayList<FileData> files) {
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public FileData getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.view_item_file, parent, false);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView sizeAndDateTextView = (TextView) convertView.findViewById(R.id.sizeAndDateTextView);
        LinearLayout documentInfo = (LinearLayout) convertView.findViewById(R.id.documentInfo);
        ImageButton fileOptionsButton = (ImageButton) convertView.findViewById(R.id.fileOptionsButton);
        ImageView imageIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
        ImageView storedStatusImage = (ImageView) convertView.findViewById(R.id.storedStatusImage);
        FileData fileData = files.get(position);
        nameTextView.setText(fileData.getName());
        if (fileData.getType() == Type.FILE)
            sizeAndDateTextView.setText(fileData.displaySize() + ", " + fileData.displayDate());
        documentInfo.setTag(position);
        fileOptionsButton.setTag(position);
        storedStatusImage.setImageResource(fileData.isOnDevice() ? R.mipmap.on_device_icon : 0);
        if (fileData.getType() == Type.DIRECTORY)
            imageIcon.setImageResource(R.drawable.folder_icon);
        return convertView;
    }
}
