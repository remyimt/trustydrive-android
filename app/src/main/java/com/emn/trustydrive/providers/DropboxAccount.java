package com.emn.trustydrive.providers;

import java.io.File;

public class DropboxAccount extends CloudAccount {

    public DropboxAccount(String email) {
        super(email);
    }

    @Override
    public File getMetadata(String password) {
        return null;
    }

    @Override
    public void updateMetadata(File metadata, String passwordOrName) {

    }

    @Override
    public void uploadFile(File file) {

    }

    @Override
    public File downloadFile(String name) {
        return null;
    }

    @Override
    public int remainingSpace() {
        return 0;
    }
}
