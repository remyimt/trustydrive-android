package com.emn.trustydrive.providers;

import java.io.File;

public interface ICloudAccount {

    File getMetadata(String password);

    void updateMetadata(File metadata, String passwordOrName);

    void uploadFile(File file);

    File downloadFile(String name);

    /**
    * Available storage in MB
    */
    int remainingSpace();

}
