package com.fuad.donasi.Data;

/**
 * Created by ASUS on 19/11/2017.
 */

public class DataPanti {
    String id_panti,nama_panti,alamat_panti,no_telp;

    public DataPanti(){}

    public DataPanti(String id_panti,String nama_panti,String alamat_panti,String no_telp){
        this.id_panti=id_panti;
        this.nama_panti=nama_panti;
        this.alamat_panti=alamat_panti;
        this.no_telp=no_telp;
    }

    public void setId_panti(String id_panti) {
        this.id_panti = id_panti;
    }

    public void setNama_panti(String nama_panti) {
        this.nama_panti = nama_panti;
    }

    public void setAlamat_panti(String alamat_panti) {
        this.alamat_panti = alamat_panti;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getId_panti() {
        return id_panti;
    }

    public String getNama_panti() {
        return nama_panti;
    }

    public String getAlamat_panti() {
        return alamat_panti;
    }

    public String getNo_telp() {
        return no_telp;
    }
}

