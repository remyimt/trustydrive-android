package com.emn.trustydrive.metadata;

import java.util.ArrayList;

public class DataHolder {
    private ArrayList<Account> accounts;
    private TrustyDrive metadata;
    private FileData file;

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public TrustyDrive getMetadata() {
        return metadata;
    }

    public void setMetadata(TrustyDrive metadata) {
        this.metadata = metadata;
    }

    public FileData getFile() {
        return file;
    }

    public void setFile(FileData file) {
        this.file = file;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }
}
