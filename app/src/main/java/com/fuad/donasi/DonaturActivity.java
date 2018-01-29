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
import com.fuad.donasi.Adapter.AdapterDonatur;
import com.fuad.donasi.App.AppController;
import com.fuad.donasi.Data.DataDonatur;
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

public class DonaturActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    Toolbar toolbar_donatur;
    FloatingActionButton fab_donatur;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataDonatur> itemList = new ArrayList<DataDonatur>();
    AdapterDonatur adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id_donatur, txt_username, txt_password, txt_nama_donatur,txt_alamat;
    String id_donatur, username, password, nama_donatur,alamat;

    private static final String TAG = DonaturActivity.class.getSimpleName();

    private static String url_select = Server.URL + "donatur/read.php";
    private static String url_insert = Server.URL + "donatur/create.php";
    private static String url_edit = Server.URL + "donatur/select.php";
    private static String url_update = Server.URL + "donatur/update.php";
    private static String url_delete = Server.URL + "donatur/delete.php";

    public static final String TAG_ID_DONATUR = "id_donatur";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_NAMA_DONATUR = "nama_donatur";
    public static final String TAG_ALAMAT = "alamat";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donatur);
        Toolbar toolbar_donatur = (Toolbar) findViewById(R.id.toolbar_donatur);
        setSupportActionBar(toolbar_donatur);

        // menghubungkan variablel pada layout dan pada java
        fab_donatur = (FloatingActionButton) findViewById(R.id.fab_donatur);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_donatur);
        list = (ListView) findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterDonatur(DonaturActivity.this, itemList);
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
        fab_donatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "", "", "SIMPAN");
            }
        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                final String id_donaturx = itemList.get(position).getId_donatur();
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(DonaturActivity.this);
                dialog.setCancelable(true);

                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_donaturx);
                                break;
                            case 1:
                                delete(id_donaturx);
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
        txt_id_donatur.setText(null);
        txt_username.setText(null);
        txt_password.setText(null);
        txt_nama_donatur.setText(null);
        txt_alamat.setText(null);
    }

    // untuk menampilkan dialog from pengguna
    private void DialogForm(String id_donaturx, String usernamex, String passwordx, String nama_donaturx, String alamatx, String button) {
        dialog = new AlertDialog.Builder(DonaturActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_donatur, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Pengguna");
        txt_id_donatur = (EditText) dialogView.findViewById(R.id.txt_id_donatur);
        txt_username = (EditText) dialogView.findViewById(R.id.txt_username);
        txt_password = (EditText) dialogView.findViewById(R.id.txt_password);
        txt_nama_donatur = (EditText) dialogView.findViewById(R.id.txt_nama_donatur);
        txt_alamat = (EditText) dialogView.findViewById(R.id.txt_alamat);

        if (!id_donaturx.isEmpty()) {
            txt_id_donatur.setText(id_donaturx);
            txt_username.setText(usernamex);
            txt_password.setText(passwordx);
            txt_nama_donatur.setText(nama_donaturx);
            txt_alamat.setText(alamatx);
        } else {
            kosong();
        }
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_donatur = txt_id_donatur.getText().toString();
                username = txt_username.getText().toString();
                password = txt_password.getText().toString();
                nama_donatur = txt_nama_donatur.getText().toString();
                alamat = txt_alamat.getText().toString();
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
                        DataDonatur item = new DataDonatur();
                        item.setId_donatur(obj.getString(TAG_ID_DONATUR));
                        item.setUsername(obj.getString(TAG_USERNAME));
                        item.setPassword(obj.getString(TAG_PASSWORD));
                        item.setNama_donatur(obj.getString(TAG_NAMA_DONATUR));
                        item.setAlamat(obj.getString(TAG_ALAMAT));

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
        if (id_donatur.isEmpty()) {
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
                                Toast.makeText(DonaturActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(DonaturActivity.this,
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
                Toast.makeText(DonaturActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_donatur.isEmpty()) {
                    params.put("username", username);
                    params.put("password", password);
                    params.put("nama_donatur", nama_donatur);
                    params.put("alamat", alamat);
                } else {
                    params.put("id_donatur", id_donatur);
                    params.put("username", username);
                    params.put("password", password);
                    params.put("nama_donatur", nama_donatur);
                    params.put("alamat", alamat);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get id_donatur untuk edit/update data
    private void edit(final String id_donaturx) {
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
                        String id_donaturx = jObj.getString(TAG_ID_DONATUR);
                        String usernamex = jObj.getString(TAG_USERNAME);
                        String passwordx = jObj.getString(TAG_PASSWORD);
                        String nama_donaturx = jObj.getString(TAG_NAMA_DONATUR);
                        String alamatx = jObj.getString(TAG_ALAMAT);

                        DialogForm(id_donaturx, usernamex, passwordx, nama_donaturx,alamatx, "UPDATE");
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DonaturActivity.this,
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
                Toast.makeText(DonaturActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_donatur", id_donaturx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String id_donaturx) {
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
                                Toast.makeText(DonaturActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(DonaturActivity.this,
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
                Toast.makeText(DonaturActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_donatur", id_donaturx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}