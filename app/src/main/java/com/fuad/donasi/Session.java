package com.fuad.donasi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ASUS on 16/01/2018.
 */

public class Session {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        editor = prefs.edit();
    }

    public void setsession(String name,String value) {
        editor.putString(name, value).commit();
    }

    public String getsession(String name) {
        return prefs.getString(name, "");
    }


}
