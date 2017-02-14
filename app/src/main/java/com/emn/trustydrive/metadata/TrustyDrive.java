package com.emn.trustydrive.metadata;

import java.util.ArrayList;

public class TrustyDrive {
    private ArrayList<FileData> files;

    public TrustyDrive() {
        files = new ArrayList<>();
    }

    public ArrayList<FileData> getFiles() {
        return files;
    }
}
