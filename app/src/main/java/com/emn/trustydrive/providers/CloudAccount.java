package com.emn.trustydrive.providers;

/**
 * Created by Maxime on 09/01/2017.
 */

public abstract class CloudAccount implements ICloudAccount {

    private String email;
    private String apiToken;

    public CloudAccount(String email) {
        this.email = email;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
