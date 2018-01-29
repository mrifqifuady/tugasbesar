package com.fuad.donasi.Data;

/**
 * Created by ASUS on 20/11/2017.
 */

public class DataLaporan {
    String id_laporan;
    String id_donatur;
    String id_panti;
    String id_admin;
    String id_donatur_pil;

    public String getId_panti_pil() {
        return id_panti_pil;
    }

    public void setId_panti_pil(String id_panti_pil) {
        this.id_panti_pil = id_panti_pil;
    }

    String id_panti_pil;

    public DataLaporan() {
    }

    public String getId_donatur_pil() {
        return id_donatur_pil;
    }

    public void setId_donatur_pil(String id_donatur_pil) {
        this.id_donatur_pil = id_donatur_pil;
    }

    public DataLaporan(String id_laporan, String id_donatur, String id_panti, String id_admin, String id_donatur_pil) {
        this.id_laporan = id_laporan;
        this.id_donatur = id_donatur;
        this.id_donatur_pil = id_donatur_pil;
        this.id_panti = id_panti;
        this.id_admin = id_admin;
    }

    public void setId_laporan(String id_laporan) {
        this.id_laporan = id_laporan;
    }

    public void setId_donatur(String id_donatur) {
        this.id_donatur = id_donatur;
    }

    public void setId_panti(String id_panti) {
        this.id_panti = id_panti;
    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
    }

    public String getId_laporan() {
        return id_laporan;
    }

    public String getId_donatur() {
        return id_donatur;
    }

    public String getId_panti() {
        return id_panti;
    }

    public String getId_admin() {
        return id_admin;
    }
}

