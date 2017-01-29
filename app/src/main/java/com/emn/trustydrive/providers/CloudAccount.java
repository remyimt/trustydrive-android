package com.emn.trustydrive.providers;

public abstract class CloudAccount implements ICloudAccount {
    private String token;
    private String email;
    private Provider provider;

    public CloudAccount(String token, String email, Provider provider) {
        this.token = token;
        this.provider = provider;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getEmail() {
        return email;
    }
}
