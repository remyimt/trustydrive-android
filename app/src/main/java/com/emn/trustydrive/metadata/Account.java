package com.emn.trustydrive.metadata;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account implements Parcelable {
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

    protected Account(Parcel in) {
        token = in.readString();
        email = in.readString();
        provider = Provider.valueOf(in.readString());
        metadataFileName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(email);
        dest.writeString(provider.name());
        dest.writeString(metadataFileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
