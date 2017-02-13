package com.emn.trustydrive.metadata;

import java.util.ArrayList;

public class FolderData implements FileOrFolderData {

    private ArrayList<FileData> files;
    private ArrayList<FolderData> folders;
    private FolderData parentFolder;
    private String name;

    public FolderData(String name) {
        this.name = name;
        this.files = new ArrayList<>();
        this.folders = new ArrayList<>();
    }

    public ArrayList<FileData> getFiles() {
        return files;
    }

    public void add(FileOrFolderData fileOrFolderData) {
        if (fileOrFolderData instanceof FileData) {
            files.add((FileData) fileOrFolderData);
        } else {
            folders.add((FolderData) fileOrFolderData);
        }
        fileOrFolderData.setParentFolder(this);
    }

    public ArrayList<FolderData> getFolders() {
        return folders;
    }

    public int getCount() {
        return files.size() + folders.size();
    }

    public String getName() {
        return name;
    }

    @Override
    public void setParentFolder(FolderData folderData) {
        this.parentFolder = folderData;
    }

    public FolderData getParentFolder() {
        return parentFolder;
    }
}
