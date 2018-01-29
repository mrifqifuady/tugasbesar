package com.fuad.donasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuad.donasi.Data.DataDetail;
import com.fuad.donasi.R;

import java.util.List;

/**
 * Created by ASUS on 20/11/2017.
 */

public class AdapterDetail extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataDetail> items;
    public AdapterDetail(Activity activity, List<DataDetail> items) {
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
            convertView = inflater.inflate(R.layout.list_detail, null);
        TextView id_donasi = (TextView) convertView.findViewById(R.id.id_donasi);
        TextView id_donatur = (TextView) convertView.findViewById(R.id.id_donatur);
        TextView id_panti = (TextView) convertView.findViewById(R.id.id_panti);
        TextView id_jenis_donasi = (TextView) convertView.findViewById(R.id.id_jenis_donasi);
        TextView jumlah = (TextView) convertView.findViewById(R.id.jumlah);
        TextView keterangan = (TextView) convertView.findViewById(R.id.keterangan);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);
        TextView status = (TextView) convertView.findViewById(R.id.status);

        DataDetail data = items.get(position);

        id_donasi.setText(data.getId_donasi());
        id_donatur.setText(data.getId_donatur());
        id_panti.setText(data.getId_panti());
        id_jenis_donasi.setText(data.getId_jenis_donasi());
        jumlah.setText(data.getJumlah());
        keterangan.setText(data.getKeterangan());
        tanggal.setText(data.getTanggal());
        status.setText(data.getStatus());

        return convertView;
    }
}