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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.fuad.donasi.Adapter.AdapterPanti;
import com.fuad.donasi.App.AppController;
import com.fuad.donasi.Data.DataPanti;
import com.fuad.donasi.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 19/11/2017.
 */

public class PantiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    Toolbar toolbar_panti;
    FloatingActionButton fab_panti;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataPanti> itemList = new ArrayList<DataPanti>();
    AdapterPanti adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id_panti, txt_nama_panti, txt_alamat_panti, txt_no_telp;
    String id_panti, nama_panti, alamat_panti, no_telp;

    private static final String TAG = PantiActivity.class.getSimpleName();

    private static String url_select = Server.Soap + "read.php";
    private static String url_insert = Server.URL + "create.php";
    private static String url_edit = Server.URL + "panti/select.php";
    private static String url_update = Server.URL + "panti/update.php";
    private static String url_delete = Server.URL + "panti/delete.php";

    public static final String TAG_ID_PANTI = "id_panti";
    public static final String TAG_NAMA_PANTI = "nama_panti";
    public static final String TAG_ALAMAT_PANTI = "alamat_panti";
    public static final String TAG_NO_TELP = "no_telp";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panti);
        Toolbar toolbar_panti = (Toolbar) findViewById(R.id.toolbar_panti);
        setSupportActionBar(toolbar_panti);

        // menghubungkan variablel pada layout dan pada java
        //fab_panti = (FloatingActionButton) findViewById(R.id.fab_panti);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_panti);
        list = (ListView) findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterPanti(PantiActivity.this, itemList);
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
//        fab_panti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogForm("", "", "", "", "SIMPAN");
//            }
//        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                final String id_pantix = itemList.get(position).getId_panti();
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(PantiActivity.this);
                dialog.setCancelable(true);

                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_pantix);
                                break;
                            case 1:
                                delete(id_pantix);
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
        txt_id_panti.setText(null);
        txt_nama_panti.setText(null);
        txt_alamat_panti.setText(null);
        txt_no_telp.setText(null);
    }

    // untuk menampilkan dialog from pengguna
    private void DialogForm(String id_pantix, String nama_pantix, String alamat_pantix, String no_telpx, String button) {
        dialog = new AlertDialog.Builder(PantiActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_panti, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Pengguna");
        txt_id_panti = (EditText) dialogView.findViewById(R.id.txt_id_panti);
        txt_nama_panti = (EditText) dialogView.findViewById(R.id.txt_nama_panti);
        txt_alamat_panti = (EditText) dialogView.findViewById(R.id.txt_alamat_panti);
        txt_no_telp = (EditText) dialogView.findViewById(R.id.txt_no_telp);

        if (!id_pantix.isEmpty()) {
            txt_id_panti.setText(id_pantix);
            txt_nama_panti.setText(nama_pantix);
            txt_alamat_panti.setText(alamat_pantix);
            txt_no_telp.setText(no_telpx);
        } else {
            kosong();
        }
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_panti = txt_id_panti.getText().toString();
                nama_panti = txt_nama_panti.getText().toString();
                alamat_panti = txt_alamat_panti.getText().toString();
                no_telp = txt_no_telp.getText().toString();
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
                        DataPanti item = new DataPanti();
                        item.setId_panti(obj.getString(TAG_ID_PANTI));
                        item.setNama_panti(obj.getString(TAG_NAMA_PANTI));
                        item.setAlamat_panti(obj.getString(TAG_ALAMAT_PANTI));
                        item.setNo_telp(obj.getString(TAG_NO_TELP));

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
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_panti.isEmpty()) {
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
                                Toast.makeText(PantiActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PantiActivity.this,
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
                Toast.makeText(PantiActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_panti.isEmpty()) {
                    params.put("nama_panti", nama_panti);
                    params.put("alamat_panti", alamat_panti);
                    params.put("no_telp", no_telp);
                } else {
                    params.put("id_panti", id_panti);
                    params.put("nama_panti", nama_panti);
                    params.put("alamat_panti", alamat_panti);
                    params.put("no_telp", no_telp);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get id_panti untuk edit/update data
    private void edit(final String id_pantix) {
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
                        String id_pantix = jObj.getString(TAG_ID_PANTI);
                        String nama_pantix = jObj.getString(TAG_NAMA_PANTI);
                        String alamat_pantix = jObj.getString(TAG_ALAMAT_PANTI);
                        String no_telpx = jObj.getString(TAG_NO_TELP);

                        DialogForm(id_pantix, nama_pantix, alamat_pantix, no_telpx, "UPDATE");
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(PantiActivity.this,
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
                Toast.makeText(PantiActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_panti", id_pantix);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String id_pantix) {
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
                                Toast.makeText(PantiActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PantiActivity.this,
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
                Toast.makeText(PantiActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_panti", id_pantix);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}