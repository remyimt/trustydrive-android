package com.emn.trustydrive.providers;

/**
 * Needed for serialization (in order to save), because we can't serialize abstract classes
 */
public class CloudAccountData {
    private String token;
    private Provider provider;
    private String email;

    public CloudAccountData(String token, Provider provider, String email) {
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
