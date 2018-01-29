package com.fuad.donasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuad.donasi.Data.DataPanti;
import com.fuad.donasi.R;

import java.util.List;

/**
 * Created by ASUS on 19/11/2017.
 */

public class AdapterPanti extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataPanti> items;
    public AdapterPanti(Activity activity, List<DataPanti> items) {
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
            convertView = inflater.inflate(R.layout.list_panti, null);
        TextView id_panti = (TextView) convertView.findViewById(R.id.id_panti);
        TextView nama_panti = (TextView) convertView.findViewById(R.id.nama_panti);
        TextView alamat_panti = (TextView) convertView.findViewById(R.id.alamat_panti);
        TextView no_telp = (TextView) convertView.findViewById(R.id.no_telp);

        DataPanti data = items.get(position);

        id_panti.setText(data.getId_panti());
        nama_panti.setText(data.getNama_panti());
        alamat_panti.setText(data.getAlamat_panti());
        no_telp.setText(data.getNo_telp());

        return convertView;
    }
}