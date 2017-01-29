package com.emn.trustydrive.metadata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TrustyDrive implements Parcelable {
    private ArrayList<FileData> filesData;

    public TrustyDrive() {
        filesData = new ArrayList<>();
    }

    public ArrayList<FileData> getFilesData() {
        return filesData;
    }

    protected TrustyDrive(Parcel in) {
        filesData = in.createTypedArrayList(FileData.CREATOR);
    }

    public static final Creator<TrustyDrive> CREATOR = new Creator<TrustyDrive>() {
        @Override
        public TrustyDrive createFromParcel(Parcel in) {
            return new TrustyDrive(in);
        }

        @Override
        public TrustyDrive[] newArray(int size) {
            return new TrustyDrive[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(filesData);
    }
}
