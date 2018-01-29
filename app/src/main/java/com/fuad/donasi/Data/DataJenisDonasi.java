package com.fuad.donasi.Data;

/**
 * Created by ASUS on 20/11/2017.
 */

public class DataJenisDonasi {
    String id_jenis_donasi,jenis_donasi;

    public DataJenisDonasi(){}

    public DataJenisDonasi(String id_jenis_donasi, String jenis_donasi){
        this.id_jenis_donasi =id_jenis_donasi;
        this.jenis_donasi=jenis_donasi;
    }

    public void setId_jenis_donasi(String id_jenis_donasi) {
        this.id_jenis_donasi = id_jenis_donasi;
    }

    public void setJenis_donasi(String jenis_donasi) {
        this.jenis_donasi = jenis_donasi;
    }

    public String getId_jenis_donasi() {
        return id_jenis_donasi;
    }

    public String getJenis_donasi() {
        return jenis_donasi;
    }
}

