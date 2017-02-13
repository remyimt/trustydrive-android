package com.emn.trustydrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emn.trustydrive.R;
import com.emn.trustydrive.metadata.ChunkData;
import com.emn.trustydrive.metadata.FileData;
import com.emn.trustydrive.metadata.FileOrFolderData;
import com.emn.trustydrive.metadata.FolderData;
import com.emn.trustydrive.metadata.Type;

import java.util.ArrayList;
import java.util.Date;

public class FolderAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private FolderData rootFolder;
    private FolderData currentFolder;

    public FolderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        rootFolder = new FolderData("root folder");
        this.currentFolder = rootFolder;

        FileData file1 = new FileData("file1", new Date().getTime(), Type.FILE, "", 123, new ArrayList<ChunkData>(), null);
        FileData file2 = new FileData("file2", new Date().getTime(), Type.FILE, "", 123, new ArrayList<ChunkData>(), null);
        FileData file1_1 = new FileData("file1_1", new Date().getTime(), Type.FILE, "", 123, new ArrayList<ChunkData>(), null);
        FileData file1_1_1 = new FileData("file1_1_1", new Date().getTime(), Type.FILE, "", 123, new ArrayList<ChunkData>(), null);
        FileData file2_1 = new FileData("file2_1", new Date().getTime(), Type.FILE, "", 123, new ArrayList<ChunkData>(), null);

        FolderData folder1 = new FolderData("folder1");
        FolderData folder1_1 = new FolderData("folder1_1");
        rootFolder.add(file1);
        rootFolder.add(file2);
        rootFolder.add(folder1);
        folder1.add(file1_1);
        folder1.add(folder1_1);
        folder1_1.add(file1_1_1);

        FolderData folder2 = new FolderData("folder2");
        rootFolder.add(folder2);
        folder2.add(file2_1);
    }

    @Override
    public int getCount() {
        return currentFolder.getCount();
    }

    @Override
    public FileOrFolderData getItem(int position) {
        if (position < currentFolder.getFolders().size()) {
            return currentFolder.getFolders().get(position);
        } else {
            return currentFolder.getFiles().get(position - currentFolder.getFolders().size());
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.file_or_folder_item, parent, false);
        TextView nameTextView =(TextView) convertView.findViewById(R.id.nameTextView);
        LinearLayout linearLayout =(LinearLayout) convertView.findViewById(R.id.linearLayout);
        linearLayout.setTag(position);
        if (position < currentFolder.getFolders().size()) {
            ImageView imageIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
            imageIcon.setImageResource(R.drawable.folder_icon);
            nameTextView.setText(((FolderData) getItem(position)).getName());
        } else {
            nameTextView.setText(((FileData) getItem(position)).getName());
        }
        return convertView;
    }

    public void setCurrentFolder(Object currentFolder) {
        this.currentFolder = (FolderData) currentFolder;
    }

    public FolderData getRootFolder() {
        return rootFolder;
    }

    public FolderData getCurrentFolder() {
        return currentFolder;
    }
}
