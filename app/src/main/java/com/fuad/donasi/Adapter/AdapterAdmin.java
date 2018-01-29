package com.fuad.donasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuad.donasi.Data.DataAdmin;
import com.fuad.donasi.R;

import java.util.List;

/**
 * Created by ASUS on 20/11/2017.
 */

public class AdapterAdmin extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataAdmin> items;
    public AdapterAdmin(Activity activity, List<DataAdmin> items) {
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
            convertView = inflater.inflate(R.layout.list_admin, null);
        TextView id_admin = (TextView) convertView.findViewById(R.id.id_admin);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView password = (TextView) convertView.findViewById(R.id.password);
       

        DataAdmin data = items.get(position);

        id_admin.setText(data.getId_admin());
        username.setText(data.getUsername());
        password.setText(data.getPassword());
        

        return convertView;
    }
}