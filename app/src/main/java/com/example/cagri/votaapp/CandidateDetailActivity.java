package com.example.cagri.votaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class CandidateDetailActivity extends AppCompatActivity {

    ImageView image;
    TextView name;
    TextView party;
    String type;
    Candidate candidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_detail);

        if(Singleton.getElectorId() == null){
            Intent it = new Intent(this, AuthActivity.class);
            startActivity(it);
        } else {
            Intent it = getIntent();
            type = it.getStringExtra("type");

            candidate = Singleton.getDetail();

            image = (ImageView) findViewById(R.id.image);
            name = (TextView) findViewById(R.id.name);
            party = (TextView) findViewById(R.id.party);

            name.setText(candidate.getName());
            party.setText(candidate.getParty());
            image.setImageBitmap(candidate.getImage());
        }
    }

    public void vote (View view){

        if (type.equals("prefeito")){
            Singleton.setMayor(candidate);
        } else {
            Singleton.setCouncilman(candidate);
        }

        Toast.makeText(this, "Voto adicionado. Não esqueça de confirmar.", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent it = new Intent(CandidateDetailActivity.this, WelcomeActivity.class);
                startActivity(it);
            }
        }, 2000);

    }
}
