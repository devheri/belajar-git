package com.example.iso8583server;

public class ISOmessage_gettersetter {
    private String ipclient;

    public ISOmessage_gettersetter(String ipclient, String isomessage) {
        this.ipclient = ipclient;
        this.isomessage = isomessage;
    }

    private String isomessage;

    public String getIpclient() {
        return ipclient;
    }

    public void setIpclient(String ipclient) {
        this.ipclient = ipclient;
    }

    public String getIsomessage() {
        return isomessage;
    }

    public void setIsomessage(String isomessage) {
        this.isomessage = isomessage;
    }
}
