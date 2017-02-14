package com.emn.trustydrive.metadata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileData {
    private String name;
    private Long uploadDate;
    private Type type;
    private String absolutePath;
    private int size;
    private List<ChunkData> chunks;
    private ArrayList<FileData> files;

    private transient boolean onDevice = false;

    private final static transient SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public FileData(String name, Long uploadDate, Type type, String absolutePath, int size, List<ChunkData> chunks, ArrayList<FileData> files) {
        this.name = name;
        this.uploadDate = uploadDate;
        this.type = type;
        this.absolutePath = absolutePath;
        this.size = size;
        this.chunks = chunks;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String displayDate() {
        return dateFormat.format(new Date(uploadDate));
    }

    public Type getType() {
        return type;
    }

    public String displaySize() {
        return size + " bytes";
    }

    public List<ChunkData> getChunks() {
        return chunks;
    }

    public ArrayList<FileData> getFiles() {
        return files;
    }

    public boolean isOnDevice() {
        return onDevice;
    }

    public void setOnDevice(boolean onDevice) {
        this.onDevice = onDevice;
    }
}
