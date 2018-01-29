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
import com.fuad.donasi.Adapter.AdapterJenisDonasi;
import com.fuad.donasi.App.AppController;
import com.fuad.donasi.Data.DataJenisDonasi;
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

public class JenisDonasiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    Toolbar toolbar_jenis_donasi;
    FloatingActionButton fab_jenis_donasi;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataJenisDonasi> itemList = new ArrayList<DataJenisDonasi>();
    AdapterJenisDonasi adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id_jenis_donasi, txt_jenis_donasi;
    String id_jenis_donasi, jenis_donasi;

    private static final String TAG = JenisDonasiActivity.class.getSimpleName();

    private static String url_select = Server.URL + "jenis_donasi/read.php";
    private static String url_insert = Server.URL + "jenis_donasi/create.php";
    private static String url_edit = Server.URL + "jenis_donasi/select.php";
    private static String url_update = Server.URL + "jenis_donasi/update.php";
    private static String url_delete = Server.URL + "jenis_donasi/delete.php";

    public static final String TAG_ID_JENIS_DONASI = "id_jenis_donasi";
    public static final String TAG_JENIS_DONASI = "jenis_donasi";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis_donasi);
        Toolbar toolbar_jenis_donasi = (Toolbar) findViewById(R.id.toolbar_jenis_donasi);
        setSupportActionBar(toolbar_jenis_donasi);

        // menghubungkan variablel pada layout dan pada java
        //fab_jenis_donasi = (FloatingActionButton) findViewById(R.id.fab_jenis_donasi);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_jenis_donasi);
        list = (ListView) findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterJenisDonasi(JenisDonasiActivity.this, itemList);
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
//        fab_jenis_donasi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogForm("", "", "SIMPAN");
//            }
//        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                final String id_jenis_donasix = itemList.get(position).getId_jenis_donasi();
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(JenisDonasiActivity.this);
                dialog.setCancelable(true);

                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_jenis_donasix);
                                break;
                            case 1:
                                delete(id_jenis_donasix);
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
        txt_id_jenis_donasi.setText(null);
        txt_jenis_donasi.setText(null);
    }

    // untuk menampilkan dialog from lapangan
    private void DialogForm(String id_jenis_donasix, String jenis_donasix, String button) {
        dialog = new AlertDialog.Builder(JenisDonasiActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_jenis_donasi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Jenis Donasi");
        txt_id_jenis_donasi = (EditText) dialogView.findViewById(R.id.txt_id_jenis_donasi);
        txt_jenis_donasi = (EditText) dialogView.findViewById(R.id.txt_jenis_donasi);

        if (!id_jenis_donasix.isEmpty()) {
            txt_id_jenis_donasi.setText(id_jenis_donasix);
            txt_jenis_donasi.setText(jenis_donasix);
        } else {
            kosong();
        }
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_jenis_donasi = txt_id_jenis_donasi.getText().toString();
                jenis_donasi = txt_jenis_donasi.getText().toString();
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
                        DataJenisDonasi item = new DataJenisDonasi();
                        item.setId_jenis_donasi(obj.getString(TAG_ID_JENIS_DONASI));
                        item.setJenis_donasi(obj.getString(TAG_JENIS_DONASI));

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
        if (id_jenis_donasi.isEmpty()) {
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
                                Toast.makeText(JenisDonasiActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(JenisDonasiActivity.this,
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
                Toast.makeText(JenisDonasiActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_jenis_donasi.isEmpty()) {
                    params.put("jenis_donasi", jenis_donasi);
                } else {
                    params.put("id_jenis_donasi", id_jenis_donasi);
                    params.put("jenis_donasi", jenis_donasi);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get id_jenis_donasi untuk edit/update data
    private void edit(final String id_jenis_donasix) {
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
                        String id_jenis_donasix = jObj.getString(TAG_ID_JENIS_DONASI);
                        String jenis_donasix = jObj.getString(TAG_JENIS_DONASI);

                        DialogForm(id_jenis_donasix, jenis_donasix, "UPDATE");
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(JenisDonasiActivity.this,
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
                Toast.makeText(JenisDonasiActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_jenis_donasi", id_jenis_donasix);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String id_jenis_donasix) {
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
                                Toast.makeText(JenisDonasiActivity.this,
                                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(JenisDonasiActivity.this,
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
                Toast.makeText(JenisDonasiActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_jenis_donasi", id_jenis_donasix);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}

