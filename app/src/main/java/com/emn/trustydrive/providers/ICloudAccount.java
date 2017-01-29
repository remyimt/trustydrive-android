package com.emn.trustydrive.providers;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface ICloudAccount {

    FileInputStream getMetadata(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException;

    void updateMetadata(FileInputStream metadata);

    void uploadFile(FileInputStream file, String fileName);

    FileInputStream downloadFile(String name);

    /**
     * Available storage in MB
     */
    int remainingSpace();
}
