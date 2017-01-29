package com.emn.trustydrive.providers.google_drive;

import com.emn.trustydrive.providers.CloudAccount;
import com.emn.trustydrive.providers.Provider;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class GoogleDriveAccount extends CloudAccount {

    public GoogleDriveAccount(String token, String email) {
        super(token, email, Provider.GOOGLE_DRIVE);
    }

    @Override
    public FileInputStream getMetadata(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return null;
    }

    @Override
    public void updateMetadata(FileInputStream metadata) {

    }

    @Override
    public void uploadFile(FileInputStream file, String fileName) {

    }

    @Override
    public FileInputStream downloadFile(String name) {
        return null;
    }

    @Override
    public int remainingSpace() {
        return 0;
    }
}
