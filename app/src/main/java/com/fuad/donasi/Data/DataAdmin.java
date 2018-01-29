package com.fuad.donasi.Data;

/**
 * Created by ASUS on 20/11/2017.
 */

public class DataAdmin {
    String id_admin,username,password;

    public DataAdmin(){}

    public DataAdmin(String id_admin,String username,String password){
        this.id_admin=id_admin;
        this.username=username;
        this.password=password;

    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getId_admin() {
        return id_admin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
