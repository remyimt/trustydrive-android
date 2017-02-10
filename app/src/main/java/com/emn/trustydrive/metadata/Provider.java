package com.emn.trustydrive.metadata;

import com.google.gson.annotations.SerializedName;

public enum Provider {
    @SerializedName("Dropbox")
    DROPBOX,
    @SerializedName("Google Drive")
    GOOGLE_DRIVE,
    @SerializedName("OneDrive")
    ONEDRIVE;
}
