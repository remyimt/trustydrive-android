package com.emn.trustydrive.metadata;

public class ChunkData {
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

    public void setName(String name) {
        this.name = name;
    }
}
