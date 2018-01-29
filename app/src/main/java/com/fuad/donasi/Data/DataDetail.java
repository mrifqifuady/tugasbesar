package com.fuad.donasi.Data;

/**
 * Created by ASUS on 20/11/2017.
 */

public class DataDetail {
    String id_donasi,id_donatur,id_panti,id_jenis_donasi,jumlah,keterangan,tanggal,status;

    public DataDetail(){}

    public DataDetail(String id_donasi,String id_donatur,String id_panti,String id_jenis_donasi,String jumlah,String keterangan,String tanggal,String status){
        this.id_donasi=id_donasi;
        this.id_donatur=id_donatur;
        this.id_panti=id_panti;
        this.id_jenis_donasi=id_jenis_donasi;
        this.jumlah=jumlah;
        this.keterangan=keterangan;
        this.tanggal=tanggal;
        this.status=status;
    }

    public String getId_donasi() {
        return id_donasi;
    }

    public void setId_donasi(String id_donasi) {
        this.id_donasi = id_donasi;
    }

    public String getId_donatur() {
        return id_donatur;
    }

    public void setId_donatur(String id_donatur) {
        this.id_donatur = id_donatur;
    }

    public String getId_panti() {
        return id_panti;
    }

    public void setId_panti(String id_panti) {
        this.id_panti = id_panti;
    }

    public String getId_jenis_donasi() {
        return id_jenis_donasi;
    }

    public void setId_jenis_donasi(String id_jenis_donasi) {
        this.id_jenis_donasi = id_jenis_donasi;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}