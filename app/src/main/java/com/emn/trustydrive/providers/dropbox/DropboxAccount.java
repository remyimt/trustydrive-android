package com.emn.trustydrive.providers.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.emn.trustydrive.providers.CloudAccount;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DropboxAccount extends CloudAccount {
    private DbxClientV2 client;
    private String metaDataFileName;

    public DropboxAccount(String token, String email) {
        super(token, email, CloudAccount.DROPBOX);
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("trustyDrive").build();
        client = new DbxClientV2(requestConfig, token);
    }

    @Override
    public FileInputStream getMetadata(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update("dropbox".getBytes("UTF-8"));
        digest.update(this.getEmail().getBytes("UTF-8"));
        digest.update(password.getBytes("UTF-8"));
        byte[] byteData = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData)
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        metaDataFileName = sb.toString();
        return this.downloadFile(sb.toString());
    }

    @Override
    public void updateMetadata(FileInputStream metadata) {
        this.uploadFile(metadata, metaDataFileName);
    }

    @Override
    public void uploadFile(FileInputStream file, String fileName) {
        new UploadTask(client, new UploadTask.Callback() {
            public void onTaskComplete(FileMetadata result) {
                //ToDo: Display success message
            }

            public void onError(Exception e) {
                //ToDo: Explain error to user
                e.printStackTrace();
            }
        }).execute(file, fileName);
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
