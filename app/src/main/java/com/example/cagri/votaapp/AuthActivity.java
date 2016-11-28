package com.example.cagri.votaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {

    public static final String REQUEST_TAG = "MainVolleyActivity";

    private EditText elector;
    private EditText password;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        elector = (EditText) findViewById(R.id.elector);
        password = (EditText) findViewById(R.id.password);

    }

    protected void onClick(View view) {
        if (elector.getText().length() == 0 || password.getText().length() == 0) {
            Toast.makeText(this, "Preencha Título de eleitor e senha.", Toast.LENGTH_SHORT).show();
        } else {
            mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

            String url = "http://10.10.10.36:7780/auth/" + elector.getText() + "/password/" + password.getText();

            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(
                    Request.Method.GET,
                    url,
                    new JSONObject(),
                    this,
                    this);
            jsonRequest.setTag(REQUEST_TAG);

            mQueue.add(jsonRequest);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Erro: " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            Integer code = ((JSONObject) response).getInt("code");
            String message = ((JSONObject) response).getString("message");
            if (code == 0) {
                Singleton.setElectorId(elector.getText().toString());
                Intent it = new Intent(this, WelcomeActivity.class);
                startActivity(it);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
