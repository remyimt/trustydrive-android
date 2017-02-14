package com.emn.trustydrive.metadata;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account implements Comparable {
    private String token;
    private String email;
    private Provider provider;
    private transient String metadataFileName; // transient -> not put in JSON

    public Account(String token, String email, Provider provider) {
        this.token = token;
        this.email = email;
        this.provider = provider;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMetadataFileName() {
        return metadataFileName;
    }

    public void setMetadataFileName(String metadataFileName) {
        this.metadataFileName = metadataFileName;
    }

    public String createHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(this.getProvider().name().getBytes());
            digest.update(this.getEmail().getBytes("UTF-8"));
            digest.update(password.getBytes("UTF-8"));
            byte[] byteData = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByteData : byteData)
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int compareTo(Object o) {
        return (getProvider().name()+getEmail()).compareTo(((Account) o).getProvider().name()+((Account) o).getEmail());
    }
}
