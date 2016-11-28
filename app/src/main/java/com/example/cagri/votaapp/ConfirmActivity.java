package com.example.cagri.votaapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {

    Candidate mayor = null;
    Candidate councilman = null;

    ImageView mayorPic;
    TextView mayorName;
    TextView mayorParty;

    ImageView councilmanPic;
    TextView councilmanName;
    TextView councilmanParty;

    private RequestQueue mQueue;

    public static final String REQUEST_TAG = "MainVolleyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        if(Singleton.getElectorId() == null){
            Intent it = new Intent(this, AuthActivity.class);
            startActivity(it);
        } else {
            mayor = Singleton.getMayor();
            councilman = Singleton.getCouncilman();

            mayorPic = (ImageView) findViewById(R.id.mayorPic);
            mayorName = (TextView) findViewById(R.id.mayorName);
            mayorParty = (TextView) findViewById(R.id.mayorParty);
            councilmanPic = (ImageView) findViewById(R.id.councilmanPic);
            councilmanName = (TextView) findViewById(R.id.councilmanName);
            councilmanParty = (TextView) findViewById(R.id.councilmanParty);

            if(mayor != null){
                mayorPic.setImageBitmap(mayor.getImage());
                mayorName.setText(mayor.getName());
                mayorParty.setText(mayor.getParty());
            }

            if(councilman != null){
                councilmanPic.setImageBitmap(councilman.getImage());
                councilmanName.setText(councilman.getName());
                councilmanParty.setText(councilman.getParty());
            }
        }
    }

    protected void vote(View view) {
        if (mayor == null || councilman == null) {
            Toast.makeText(this, "Para confirmar o voto, escolha um prefeito e um vereador.", Toast.LENGTH_LONG).show();
        } else {
            mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

            String url = "http://10.10.10.36:7780/electors/" + Singleton.getElectorId() + "/votes";
            JSONObject vote = new JSONObject();

            try{
                vote.put("mayorId", mayor.getId());
                vote.put("councilmanId", councilman.getId());

                final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(
                        Request.Method.POST,
                        url,
                        vote,
                        this,
                        this);
                jsonRequest.setTag(REQUEST_TAG);

                mQueue.add(jsonRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
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
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                }, 3500);

            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
