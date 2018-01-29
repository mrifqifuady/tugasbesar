package com.fuad.donasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuad.donasi.Data.DataJenisDonasi;
import com.fuad.donasi.R;

import java.util.List;

/**
 * Created by ASUS on 20/11/2017.
 */

public class AdapterJenisDonasi extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataJenisDonasi> items;
    public AdapterJenisDonasi(Activity activity, List<DataJenisDonasi> items) {
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
            convertView = inflater.inflate(R.layout.list_jenis_donasi, null);
        TextView id_jenis_donasi = (TextView) convertView.findViewById(R.id.id_jenis_donasi);
        TextView jenis_donasi = (TextView) convertView.findViewById(R.id.jenis_donasi);

        DataJenisDonasi data = items.get(position);

        id_jenis_donasi.setText(data.getId_jenis_donasi());
        jenis_donasi.setText(data.getJenis_donasi());
        return convertView;
    }
}
