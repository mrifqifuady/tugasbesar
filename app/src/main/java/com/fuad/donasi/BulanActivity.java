package com.fuad.donasi;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.fuad.donasi.Adapter.AdapterDetail;
import com.fuad.donasi.App.AppController;
import com.fuad.donasi.Data.DataDetail;
import com.fuad.donasi.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 20/11/2017.
 */

public class BulanActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar_detail;
    FloatingActionButton fab_detail;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataDetail> itemList = new ArrayList<DataDetail>();
    AdapterDetail adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    Spinner spinner_id_jenis_donasi, spinner_tahun, spinner_bulan;
    String[] idjenis_donasi, jenis_donasi;
    EditText txt_id_donasi, txt_id_donatur, txt_id_panti, txt_id_jenis_donasi, txt_jumlah, txt_keterangan, txt_tanggal;
    String id_donasi, id_donatur, id_panti, id_jenis_donasi, jumlah, keterangan, tanggal, status;
    Calendar calendar = Calendar.getInstance();
    private static final String TAG = DetailActivity.class.getSimpleName();

    private static String url_select = Server.URL + "detail_laporan/read.php";
    private static String url_select_bulan = Server.URL + "detail_laporan/read_bulan.php";
    private static String url_select_donatur = Server.URL + "detail_laporan/select_donatur.php";
    private static String url_insert = Server.URL + "detail_laporan/create.php";
    private static String url_edit = Server.URL + "detail_laporan/select.php";
    private static String url_update = Server.URL + "detail_laporan/update.php";
    private static String url_delete = Server.URL + "detail_laporan/delete.php";
    private static String url_select_jenis_donasi = Server.URL + "jenis_donasi/read.php";

    public static final String TAG_ID_DONASI = "id_donasi";
    public static final String TAG_ID_DONATUR = "id_donatur";
    public static final String TAG_ID_PANTI = "id_panti";
    public static final String TAG_ID_JENIS_DONASI = "id_jenis_donasi";
    public static final String TAG_JUMLAH = "jumlah";
    public static final String TAG_KETERANGAN = "keterangan";
    public static final String TAG_TANGGAL = "tanggal";
    public static final String TAG_NAMA_DONATUR = "nama_donatur";
    public static final String TAG_JENIS_DONASI = "jenis_donasi";
    public static final String TAG_NAMA_PANTI = "nama_panti";
    public static final String TAG_STATUS = "status";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    ArrayAdapter<String> spinnerArrayAdapter;
    String tag_json_obj = "json_obj_req";
    int bulan, tahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpbulan);
        Toolbar toolbar_detail = (Toolbar) findViewById(R.id.toolbar_detail);
        toolbar_detail.setTitle("Rekapitulasi Donasi");
        setSupportActionBar(toolbar_detail);
        // menghubungkan variablel pada layout dan pada java
        fab_detail = (FloatingActionButton) findViewById(R.id.fab_detail);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_detail);
        list = (ListView) findViewById(R.id.list);
        bulan = calendar.get(Calendar.MONTH);
        tahun = calendar.get(Calendar.YEAR);
        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterDetail(BulanActivity.this, itemList);
        list.setAdapter(adapter);
        spinner_tahun = (Spinner) findViewById(R.id.spinner_tahun);
        spinner_bulan = (Spinner) findViewById(R.id.spinner_bulan);
        String[] bulanlist = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        final String[] tahunlist = new String[10];
        int index = 0;
        for (int i = 5; i >= 0; i--) {
            tahunlist[index] = "" + (tahun - i);
            index++;
        }
        for (int i = 1; i < 5; i++) {
            tahunlist[index] = "" + (tahun + i);
            index++;
        }
        ArrayAdapter<String> spinnerArrayAdapterStatus = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, tahunlist);
        spinner_tahun.setAdapter(spinnerArrayAdapterStatus);
        spinner_tahun.setSelection(spinnerArrayAdapterStatus.getPosition("" + tahun));
        ArrayAdapter<String> spinnerArrayAdapterStatus2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, bulanlist);
        spinner_bulan.setAdapter(spinnerArrayAdapterStatus2);
        spinner_bulan.setSelection(bulan);
        spinner_tahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tahun = Integer.parseInt(tahunlist[position]);
                bulan = spinner_bulan.getSelectedItemPosition();
                callVolley();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_bulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tahun = Integer.parseInt(tahunlist[spinner_tahun.getSelectedItemPosition()]);
                bulan = spinner_bulan.getSelectedItemPosition();
                callVolley();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        fab_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "", "", "", "", "SIMPAN");
            }
        });


        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                final String id_donasix = itemList.get(position).getId_donasi();
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(BulanActivity.this);
                dialog.setCancelable(true);

                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_donasix);
                                break;
                            case 1:
                                delete(id_donasix);
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
        txt_id_donasi.setText(null);
        txt_id_donatur.setText(null);
        txt_id_panti.setText(null);
//        txt_id_jenis_donasi.setText(null);
        txt_jumlah.setText(null);
        txt_keterangan.setText(null);
        txt_tanggal.setText(null);
    }

    // untuk menampilkan dialog from pengguna
    private void DialogForm(String id_donasix, String id_donaturx, String id_pantix, String id_jenis_donasix, String jumlahx, String keteranganx, String tanggalx, String button) {
        ambil_jenisdonasi(id_jenis_donasix);
        dialog = new AlertDialog.Builder(BulanActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_detail, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Pengguna");
        txt_id_donasi = (EditText) dialogView.findViewById(R.id.txt_id_donasi);
        txt_id_donatur = (EditText) dialogView.findViewById(R.id.txt_id_donatur);
        txt_id_panti = (EditText) dialogView.findViewById(R.id.txt_id_panti);
        spinner_id_jenis_donasi = (Spinner) dialogView.findViewById(R.id.spin_id_jenis_donasi);
        txt_jumlah = (EditText) dialogView.findViewById(R.id.txt_jumlah);
        txt_keterangan = (EditText) dialogView.findViewById(R.id.txt_keterangan);
        txt_tanggal = (EditText) dialogView.findViewById(R.id.txt_tanggal);


        if (!id_donasix.isEmpty()) {
            txt_id_donasi.setText(id_donasix);
            txt_id_donatur.setText(id_donaturx);
            txt_id_panti.setText(id_pantix);

            txt_jumlah.setText(jumlahx);
            txt_keterangan.setText(keteranganx);
            txt_tanggal.setText(tanggalx);
        } else {
            kosong();
        }
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_donasi = txt_id_donasi.getText().toString();
                id_donatur = txt_id_donatur.getText().toString();
                id_panti = txt_id_panti.getText().toString();
                id_jenis_donasi = idjenis_donasi[spinner_id_jenis_donasi.getSelectedItemPosition()] + "";
                jumlah = txt_jumlah.getText().toString();
                keterangan = txt_keterangan.getText().toString();
                tanggal = txt_tanggal.getText().toString();
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

    private void ambil_jenisdonasi(final String id_jenis_donasi) {
        // membuat request JSON
        final List<String> temp_idjenis_donasi, temp_jenis_donasi;
        temp_idjenis_donasi = new ArrayList<String>();
        temp_jenis_donasi = new ArrayList<String>();
        JsonArrayRequest jArr = new JsonArrayRequest(url_select_jenis_donasi, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                idjenis_donasi = new String[response.length()];
                jenis_donasi = new String[response.length()];
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        temp_idjenis_donasi.add(obj.getString("id_jenis_donasi"));
                        temp_jenis_donasi.add(obj.getString("jenis_donasi"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                temp_idjenis_donasi.toArray(idjenis_donasi);
                temp_jenis_donasi.toArray(jenis_donasi);
                // notifikasi adanya perubahan data pada adapter
                spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, jenis_donasi);
                spinner_id_jenis_donasi.setAdapter(spinnerArrayAdapter);
                if (!id_jenis_donasi.isEmpty()) {
                    spinner_id_jenis_donasi.setSelection(spinnerArrayAdapter.getPosition(id_jenis_donasi));
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
        Log.d("URL_select", url_select_bulan);
        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select_bulan + "?tanggal=" + tahun + "-" + (bulan + 1), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        DataDetail item = new DataDetail();
                        item.setId_donasi(obj.getString(TAG_ID_DONASI));
                        item.setId_donatur(obj.getString(TAG_NAMA_DONATUR));
                        item.setId_panti(obj.getString(TAG_NAMA_PANTI));
                        item.setId_jenis_donasi(obj.getString(TAG_JENIS_DONASI));
                        item.setJumlah(obj.getString(TAG_JUMLAH));
                        item.setKeterangan(obj.getString(TAG_KETERANGAN));
                        item.setTanggal(obj.getString(TAG_TANGGAL));
                        item.setStatus(obj.getString(TAG_STATUS));

                        // menambah item ke array
                        itemList.add(item);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_donatur", id_donatur);
                return params;
            }
        };

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        final String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_donasi.isEmpty()) {
            url = url_insert;
        } else {
            url = url_update;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("Add/update", jObj.toString());
                                callVolley();
                                kosong();
                                Toast.makeText(BulanActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(BulanActivity.this,
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
                Log.d(TAG, "URL: " + url);
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(BulanActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_donasi.isEmpty()) {
                    params.put("id_jenis_donasi", id_jenis_donasi);
                    params.put("id_donatur", id_donatur);
                    params.put("id_panti", id_panti);
                    params.put("jumlah", jumlah);
                    params.put("keterangan", keterangan);
                    params.put("tanggal", tanggal);
                } else {
                    params.put("id_donasi", id_donasi);
                    params.put("id_donatur", id_donatur);
                    params.put("id_panti", id_panti);
                    params.put("id_jenis_donasi", id_jenis_donasi);
                    params.put("jumlah", jumlah);
                    params.put("keterangan", keterangan);
                    params.put("tanggal", tanggal);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get id_donasi untuk edit/update data
    private void edit(final String id_donasix) {
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
                        String id_donasix = jObj.getString(TAG_ID_DONASI);
                        String id_donaturx = jObj.getString(TAG_ID_DONATUR);
                        String id_pantix = jObj.getString(TAG_ID_PANTI);
                        String id_jenis_donasix = jObj.getString(TAG_JENIS_DONASI);
                        String jumlahx = jObj.getString(TAG_JUMLAH);
                        String keteranganx = jObj.getString(TAG_KETERANGAN);
                        String tanggalx = jObj.getString(TAG_TANGGAL);

                        DialogForm(id_donasix, id_donaturx, id_pantix, id_jenis_donasix, jumlahx, keteranganx, tanggalx, "UPDATE");
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(BulanActivity.this,
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
                Toast.makeText(BulanActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_donasi", id_donasix);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String id_donasix) {
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
                                Toast.makeText(BulanActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(BulanActivity.this,
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
                Toast.makeText(BulanActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_donasi", id_donasix);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}