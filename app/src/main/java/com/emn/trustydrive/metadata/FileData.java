package com.emn.trustydrive.metadata;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileData implements Parcelable, FileOrFolderData {
    private String name;
    private Long uploadDate;
    private Type type;
    private String absolutePath;
    private int size;
    private List<ChunkData> chunks;
    private List<FileData> files;

    private final transient SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private FolderData parentFolder;

    public FileData(String name, Long uploadDate, Type type, String absolutePath, int size, List<ChunkData> chunks, List<FileData> files) {
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

    public Long getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String displayDate() {
        return dateFormat.format(new Date(getUploadDate()));
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String displaySize() {
        return String.valueOf(getSize()) + " bytes";
    }

    public List<ChunkData> getChunks() {
        return chunks;
    }

    public void setChunks(List<ChunkData> chunksData) {
        this.chunks = chunksData;
    }

    public List<FileData> getFiles() {
        return files;
    }

    public void setFiles(List<FileData> files) {
        this.files = files;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(uploadDate);
        dest.writeString(type.name());
        dest.writeString(absolutePath);
        dest.writeInt(size);
        dest.writeTypedList(chunks);
        dest.writeTypedList(files);
    }

    protected FileData(Parcel in) {
        name = in.readString();
        uploadDate = in.readLong();
        type = Type.valueOf(in.readString());
        absolutePath = in.readString();
        size = in.readInt();
        chunks = in.createTypedArrayList(ChunkData.CREATOR);
        files = in.createTypedArrayList(FileData.CREATOR);
    }

    public static final Creator<FileData> CREATOR = new Creator<FileData>() {
        @Override
        public FileData createFromParcel(Parcel in) {
            return new FileData(in);
        }

        @Override
        public FileData[] newArray(int size) {
            return new FileData[size];
        }
    };

    public void setParentFolder(FolderData parentFolder) {
        this.parentFolder = parentFolder;
    }
}
