package com.fuad.donasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fuad.donasi.App.AppController;
import com.fuad.donasi.Util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 30/11/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText txtUsername;
    private EditText txtPassword;
    int success;
    Session session;
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String url_login = Server.URL + "donatur/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    // method oncreate yang memanggil method checkSavedCredentials dan initComponents
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        session = new Session(getApplicationContext());
        txtUsername = (EditText) findViewById(R.id.txt_username);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesLogin();
            }
        });
    }

    private void prosesLogin() {
        String url = url_login;
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Toast.makeText(LoginActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        session.setsession("id_donatur", jObj.getString("id_donatur"));
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(LoginActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", txtUsername.getText().toString());
                params.put("password", txtPassword.getText().toString());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
