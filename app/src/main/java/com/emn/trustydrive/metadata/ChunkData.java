package com.emn.trustydrive.metadata;

import android.os.Parcel;
import android.os.Parcelable;

public class ChunkData implements Parcelable {
    private Account account;
    private String name;

    public ChunkData(Account account, String name) {
        this.account = account;
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    protected ChunkData(Parcel in) {
        account = in.readParcelable(Account.class.getClassLoader());
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(account, flags);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChunkData> CREATOR = new Creator<ChunkData>() {
        @Override
        public ChunkData createFromParcel(Parcel in) {
            return new ChunkData(in);
        }

        @Override
        public ChunkData[] newArray(int size) {
            return new ChunkData[size];
        }
    };
}
