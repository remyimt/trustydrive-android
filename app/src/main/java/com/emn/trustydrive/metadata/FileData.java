package com.emn.trustydrive.metadata;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileData implements Parcelable {
    private String name;
    private Date uploadDate;
    private int size;
    private ArrayList<ChunkData> chunksData;

    public FileData(String name, Date uploadDate, int size) {
        this.name = name;
        this.uploadDate = uploadDate;
        this.size = size;
        chunksData = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String displayDate() {
        return dateFormat.format(getUploadDate());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateFormat.format(uploadDate));
        dest.writeInt(size);
        dest.writeTypedList(chunksData);
    }

    protected FileData(Parcel in) {
        name = in.readString();
        try {
            uploadDate = dateFormat.parse(in.readString());
        } catch (ParseException e) {
            uploadDate = null;
            e.printStackTrace();
        }
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
