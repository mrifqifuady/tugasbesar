package com.fuad.donasi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.fuad.donasi.Adapter.AdapterLaporan;
import com.fuad.donasi.App.AppController;
import com.fuad.donasi.Data.DataLaporan;
import com.fuad.donasi.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 20/11/2017.
 */

public class LaporanActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar_laporan;
    FloatingActionButton fab_laporan;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataLaporan> itemList = new ArrayList<DataLaporan>();
    AdapterLaporan adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id_laporan, txt_id_donatur, txt_id_panti, txt_id_admin;
    String id_laporan, id_donatur, id_panti, id_admin;
    Spinner spinner_id_donatur, spinner_id_panti, spinner_id_admin;
    private static final String TAG = LaporanActivity.class.getSimpleName();
    String[] iddonatur, nama_donatur;
    String[] idpanti, nama_panti;
    private static String url_select = Server.URL + "laporan/read.php";
    private static String url_insert = Server.URL + "laporan/create.php";
    private static String url_edit = Server.URL + "laporan/select.php";
    private static String url_update = Server.URL + "laporan/update.php";
    private static String url_delete = Server.URL + "laporan/delete.php";
    private static String url_select_donatur = Server.URL + "donatur/read.php";
    private static String url_select_panti = Server.URL + "panti/read.php";
    Session session;
    public static final String TAG_ID_LAPORAN = "id_laporan";
    public static final String TAG_ID_DONATUR = "id_donatur";
    public static final String TAG_ID_PANTI = "id_panti";
    public static final String TAG_ID_ADMIN = "id_admin";
    public static final String TAG_NAMA_DONATUR = "nama_donatur";
    public static final String TAG_NAMA_PANTI = "nama_panti";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    ArrayAdapter<String> spinnerArrayAdapter;
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        session = new Session(getApplicationContext());
        Toolbar toolbar_laporan = (Toolbar) findViewById(R.id.toolbar_laporan);
        toolbar_laporan.setTitle("Donasi");
        setSupportActionBar(toolbar_laporan);
        // menghubungkan variablel pada layout dan pada java
        fab_laporan = (FloatingActionButton) findViewById(R.id.fab_laporan);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_laporan);
        list = (ListView) findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterLaporan(LaporanActivity.this, itemList);
        list.setAdapter(adapter);

        // menampilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        // fungsi floating action button memanggil form tambah buku
        fab_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "", "SIMPAN");
            }
        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                // TODO Auto-generated method stub
                final String id_laporanx = itemList.get(position).getId_laporan();
                final CharSequence[] dialogitem = {"Edit", "Delete", "Detail"};
                dialog = new AlertDialog.Builder(LaporanActivity.this);
                dialog.setCancelable(true);

                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_laporanx);
                                break;
                            case 1:
                                delete(id_laporanx);
                                break;
                            case 2:
                                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                TextView id_donatur_pil = (TextView) view.findViewById(R.id.id_donatur_pil);
                                TextView id_panti_pil = (TextView) view.findViewById(R.id.id_panti_pil);
                                intent.putExtra("id_donatur", id_donatur_pil.getText().toString());
                                intent.putExtra("id_panti", id_panti_pil.getText().toString());
                                intent.putExtra("insert", false);
                                startActivity(intent);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk mengosongi edittext pada form
    private void kosong() {
        txt_id_laporan.setText(null);
        //spinner_id_donatur.setText(null);
        //txt_id_panti.setText(null);
//        txt_id_admin.setText(null);
    }

    // untuk menampilkan dialog from pengguna
    private void DialogForm(String id_laporanx, String id_donaturx, String id_pantix, String id_adminx, String button) {
        ambil_donatur(id_donaturx);
        ambil_panti(id_pantix);
        dialog = new AlertDialog.Builder(LaporanActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_laporan, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Donasi");
        spinner_id_donatur = (Spinner) dialogView.findViewById(R.id.spin_id_donatur);
        spinner_id_admin = (Spinner) dialogView.findViewById(R.id.spin_id_admin);
        txt_id_laporan = (EditText) dialogView.findViewById(R.id.txt_id_laporan);
        spinner_id_panti = (Spinner) dialogView.findViewById(R.id.spin_id_panti);
        txt_id_panti = (EditText) dialogView.findViewById(R.id.txt_id_panti);
        txt_id_admin = (EditText) dialogView.findViewById(R.id.txt_id_admin);
        spinner_id_donatur.setVisibility(View.GONE);
        String[] status = {"belum terkirim", "terkirim"};
        ArrayAdapter<String> spinnerArrayAdapterStatus = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, status);
        spinner_id_admin.setAdapter(spinnerArrayAdapterStatus);
        if (!id_laporanx.isEmpty()) {
            txt_id_laporan.setText(id_laporanx);
            if (!id_adminx.isEmpty()) {
                spinner_id_admin.setSelection(spinnerArrayAdapterStatus.getPosition(id_adminx));
            }
        } else {
            kosong();
        }
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_laporan = txt_id_laporan.getText().toString();
                id_donatur = iddonatur[spinner_id_donatur.getSelectedItemPosition()] + "";
                id_panti = idpanti[spinner_id_panti.getSelectedItemPosition()] + "";
                id_admin = spinner_id_admin.getSelectedItemPosition() + "";
                Log.d("status", id_admin);
                simpan_update();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });
        dialog.show();
    }


    private int cari_index_array(String cari, String[] arr) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(cari)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void ambil_donatur(final String id_donatur) {
        // membuat request JSON
        final List<String> temp_iddonatur, temp_nama_donatur;
        temp_iddonatur = new ArrayList<String>();
        temp_nama_donatur = new ArrayList<String>();
        JsonArrayRequest jArr = new JsonArrayRequest(url_select_donatur, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                iddonatur = new String[response.length()];
                nama_donatur = new String[response.length()];
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        temp_iddonatur.add(obj.getString("id_donatur"));
                        temp_nama_donatur.add(obj.getString("nama_donatur"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                temp_iddonatur.toArray(iddonatur);
                temp_nama_donatur.toArray(nama_donatur);
                int index = cari_index_array(session.getsession("id_donatur"),iddonatur);
                Toast.makeText(getApplicationContext(),""+index,Toast.LENGTH_LONG).show();
                // notifikasi adanya perubahan data pada adapter
                spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, nama_donatur);
                spinner_id_donatur.setAdapter(spinnerArrayAdapter);
                    spinner_id_donatur.setSelection(index);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    private void ambil_panti(final String id_panti) {
        // membuat request JSON
        final List<String> temp_idpanti, temp_nama_panti;
        temp_idpanti = new ArrayList<String>();
        temp_nama_panti = new ArrayList<String>();
        JsonArrayRequest jArr = new JsonArrayRequest(url_select_panti, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                idpanti = new String[response.length()];
                nama_panti = new String[response.length()];
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        temp_idpanti.add(obj.getString("id_panti"));
                        temp_nama_panti.add(obj.getString("nama_panti"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                temp_idpanti.toArray(idpanti);
                temp_nama_panti.toArray(nama_panti);
                // notifikasi adanya perubahan data pada adapter
                spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, nama_panti);
                spinner_id_panti.setAdapter(spinnerArrayAdapter);
                if (!id_panti.isEmpty()) {
                    spinner_id_panti.setSelection(spinnerArrayAdapter.getPosition(id_panti));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if (obj.getString(TAG_ID_DONATUR).equals(session.getsession("id_donatur"))) {

                            DataLaporan item = new DataLaporan();
                            item.setId_laporan(obj.getString(TAG_ID_LAPORAN));
                            item.setId_donatur(obj.getString(TAG_NAMA_DONATUR));
                            item.setId_donatur_pil(obj.getString(TAG_ID_DONATUR));
                            item.setId_panti_pil(obj.getString(TAG_ID_PANTI));
                            item.setId_panti(obj.getString(TAG_NAMA_PANTI));
                            item.setId_admin(obj.getString(TAG_ID_ADMIN));
                            itemList.add(item);
                        }
                        // menambah item ke array
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_laporan.isEmpty()) {
            url = url_insert;
        } else {
            url = url_update;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("Add/update", jObj.toString());
                                callVolley();
                                kosong();
                                Toast.makeText(LaporanActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                intent.putExtra("id_donatur", jObj.getString(TAG_ID_DONATUR));
                                intent.putExtra("id_panti", jObj.getString(TAG_ID_PANTI));
                                intent.putExtra("insert", true);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LaporanActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(LaporanActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_laporan.isEmpty()) {
                    params.put("id_donatur", id_donatur);
                    params.put("id_panti", id_panti);
                    params.put("id_admin", id_admin);
                } else {
                    params.put("id_laporan", id_laporan);
                    params.put("id_donatur", id_donatur);
                    params.put("id_panti", id_panti);
                    params.put("id_admin", id_admin);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get id_laporan untuk edit/update data
    private void edit(final String id_laporanx) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String id_laporanx = jObj.getString(TAG_ID_LAPORAN);
                        String id_donaturx = jObj.getString(TAG_NAMA_DONATUR);
                        String id_pantix = jObj.getString(TAG_NAMA_PANTI);
                        String id_adminx = jObj.getString(TAG_ID_ADMIN);

                        DialogForm(id_laporanx, id_donaturx, id_pantix, id_adminx, "UPDATE");
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LaporanActivity.this,
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(LaporanActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_laporan", id_laporanx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String id_laporanx) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("delete", jObj.toString());
                                callVolley();
                                Toast.makeText(LaporanActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(LaporanActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(LaporanActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_laporan", id_laporanx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


}