package com.emn.trustydrive.metadata;

import com.google.gson.annotations.SerializedName;

public enum Type {
    @SerializedName("file")
    FILE,
    @SerializedName("directory")
    DIRECTORY;
}
