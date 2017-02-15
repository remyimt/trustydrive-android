package com.emn.trustydrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        FileData fileData = files.get(position);
        if (fileData.getType() == Type.FILE) {
            convertView = inflater.inflate(R.layout.view_item_file, parent, false);
            ((TextView) convertView.findViewById(R.id.nameTextView)).setText(fileData.getName());
            ((TextView) convertView.findViewById(R.id.sizeAndDateTextView))
                    .setText(fileData.displaySize() + ", " + fileData.displayDate());
            convertView.findViewById(R.id.documentInfo).setTag(position);
            convertView.findViewById(R.id.fileOptionsButton).setTag(position);
            ((ImageView) convertView.findViewById(R.id.storedStatusImage))
                    .setImageResource(fileData.isOnDevice() ? R.mipmap.on_device_icon : 0);
        } else {
            convertView = inflater.inflate(R.layout.view_item_folder, parent, false);
            ((TextView) convertView.findViewById(R.id.name)).setText(fileData.getName());
            convertView.findViewById(R.id.name).setTag(position);
            convertView.findViewById(R.id.options).setTag(position);
        }
        return convertView;
    }
}
