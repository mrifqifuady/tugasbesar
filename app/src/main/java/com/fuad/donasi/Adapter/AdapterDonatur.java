package com.fuad.donasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuad.donasi.Data.DataDonatur;
import com.fuad.donasi.R;

import java.util.List;

/**
 * Created by ASUS on 19/11/2017.
 */

public class AdapterDonatur extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataDonatur> items;
    public AdapterDonatur(Activity activity, List<DataDonatur> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int location) {
        return items.get(location);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_donatur, null);
        TextView id_donatur = (TextView) convertView.findViewById(R.id.id_donatur);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView password = (TextView) convertView.findViewById(R.id.password);
        TextView nama_donatur = (TextView) convertView.findViewById(R.id.nama_donatur);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);

        DataDonatur data = items.get(position);

        id_donatur.setText(data.getId_donatur());
        username.setText(data.getUsername());
        password.setText(data.getPassword());
        nama_donatur.setText(data.getNama_donatur());
        alamat.setText(data.getAlamat());

        return convertView;
    }
}