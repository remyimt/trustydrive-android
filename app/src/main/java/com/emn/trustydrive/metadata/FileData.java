package com.emn.trustydrive.metadata;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileData implements Parcelable {
    private String name;
    private Date uploadDate;
    private String path;
    private int size;
    private List<ChunkData> chunksData;

    private final transient SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");;

    public FileData(String name, Date uploadDate, String path, int size, List<ChunkData> chunksData) {
        this.name = name;
        this.uploadDate = uploadDate;
        this.path = path;
        this.size = size;
        this.chunksData = chunksData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String displayDate() {
        return dateFormat.format(getUploadDate());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public List<ChunkData> getChunksData() {
        return chunksData;
    }

    public void setChunksData(List<ChunkData> chunksData) {
        this.chunksData = chunksData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(uploadDate.getTime());
        dest.writeString(path);
        dest.writeInt(size);
        dest.writeTypedList(chunksData);
    }

    protected FileData(Parcel in) {
        name = in.readString();
        uploadDate = new Date(in.readLong());
        path = in.readString();
        size = in.readInt();
        chunksData = in.createTypedArrayList(ChunkData.CREATOR);
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
}
