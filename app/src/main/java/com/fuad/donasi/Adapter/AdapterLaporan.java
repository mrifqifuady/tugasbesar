package com.fuad.donasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuad.donasi.Data.DataLaporan;
import com.fuad.donasi.R;

import java.util.List;

/**
 * Created by ASUS on 20/11/2017.
 */

public class AdapterLaporan extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataLaporan> items;

    public AdapterLaporan(Activity activity, List<DataLaporan> items) {
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
            convertView = inflater.inflate(R.layout.list_laporan, null);
        TextView id_laporan = (TextView) convertView.findViewById(R.id.id_laporan);
        TextView id_donatur = (TextView) convertView.findViewById(R.id.id_donatur);
        TextView id_donatur_pil = (TextView) convertView.findViewById(R.id.id_donatur_pil);
        TextView id_panti_pil = (TextView) convertView.findViewById(R.id.id_panti_pil);
        TextView id_panti = (TextView) convertView.findViewById(R.id.id_panti);
        TextView id_admin = (TextView) convertView.findViewById(R.id.id_admin);


        DataLaporan data = items.get(position);

        id_laporan.setText(data.getId_laporan());
        id_donatur.setText(data.getId_donatur());
        id_donatur_pil.setText(data.getId_donatur_pil());
        id_panti_pil.setText(data.getId_panti_pil());
        id_panti.setText(data.getId_panti());
        id_admin.setText(data.getId_admin());

        return convertView;
    }
}