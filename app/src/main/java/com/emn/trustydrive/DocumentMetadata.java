package com.emn.trustydrive;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Maxime on 07/01/2017.
 * Classe représentant les métadonnées d'un fichier.
 * (Pas sûr qu'elle soit utile pour la suite)
 */

public class DocumentMetadata implements Serializable {

    private String fileName;
    private Date date;
    private int size;
    private int id;
    private boolean storedOnDevice;

    public boolean isSavedOnDevice() {
        return storedOnDevice;
    }

    public void setStoredOnDevice(boolean storedOnDevice) {
        this.storedOnDevice = storedOnDevice;
    }

    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DocumentMetadata(String fileName, Date date, int size, int id) {
        this.fileName = fileName;
        this.date = date;
        this.size = size;
        this.id = id;
        this.storedOnDevice = false;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDate() {
        return date;
    }

    public String displayDate() {
        return dateFormat.format(getDate());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String displaySize() {
        return String.valueOf(getSize()) + " octets";
    }

    public void toggleSavedOnDeviceStatus() {
        this.storedOnDevice = !storedOnDevice;
    }
}
