package com.fuad.donasi.Data;

/**
 * Created by ASUS on 19/11/2017.
 */

public class DataDonatur{
    String id_donatur,username,password,nama_donatur,alamat;

    public DataDonatur(){}

    public DataDonatur(String id_donatur,String username,String password,String nama_donatur,String alamat){
        this.id_donatur=id_donatur;
        this.username=username;
        this.password=password;
        this.nama_donatur=nama_donatur;
        this.alamat=alamat;
    }

    public void setId_donatur(String id_donatur) {
        this.id_donatur = id_donatur;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNama_donatur(String nama_donatur) {
        this.nama_donatur = nama_donatur;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getId_donatur() {
        return id_donatur;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNama_donatur() {
        return nama_donatur;
    }

    public String getAlamat() {
        return alamat;
    }
}