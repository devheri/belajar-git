package com.example.iso8583server.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName= "IsoMessages_table")
public class EntityIsoMessages implements Serializable {
    //Membuat kolom ID sebagai Primary Key
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private
    Integer id;

    //Membuat kolom IP ADDRESS
    @ColumnInfo(name = "IP_CLIENT")
    private
    String ip_client;


    //Membuat kolom ISO MESSAGE REQUEST
    @ColumnInfo(name = "ISO_REQUEST")
    private
    String iso_request;

    //Membuat kolom ISO MESSAGE RESPON
    @ColumnInfo(name = "ISO_RESPON")
    private
    String iso_respon;


    //Method untuk mengambil data id
    @NonNull
    public Integer getId() {
        return id;
    }
    //Method untuk memasukan data ID
    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    //Method untuk mengambil data IP Client
    public String getIp_client() {
        return ip_client;
    }

    public void setIp_client(String ip_client) {
        this.ip_client = ip_client;
    }


    public String getIso_request(){
        return iso_request;
    }

    public void setIso_request(String iso_request) {
        this.iso_request = iso_request;
    }

    public String getIso_respon(){
        return iso_respon;
    }

    public void setIso_respon(String iso_respon) {
        this.iso_respon = iso_respon;
    }
}
