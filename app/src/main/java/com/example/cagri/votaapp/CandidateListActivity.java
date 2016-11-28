package com.example.cagri.votaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CandidateListActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView list;
    private String type;

    private static String url;

    ArrayList<Candidate> candidateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadidate_list);

        if(Singleton.getElectorId() == null){
            Intent it = new Intent(this, AuthActivity.class);
            startActivity(it);
        } else {
            candidateList = new ArrayList<>();
            list = (ListView) findViewById(R.id.list);

            Intent it = getIntent();

            if (it != null) {
                type = it.getStringExtra("type");
                url = "https://dl.dropboxusercontent.com/u/40990541/" + type + ".json";
            }

            new GetCandidates().execute();
        }
    }

    private class GetCandidates extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CandidateListActivity.this);
            pDialog.setMessage("Aguarde...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray candidates = jsonObj.getJSONArray(type);

                    for (int i = 0; i < candidates.length(); i++) {
                        JSONObject jsonCandidate = candidates.getJSONObject(i);

                        Candidate candidate = new Candidate();

                        candidate.setId(jsonCandidate.getString("id"));
                        candidate.setName(jsonCandidate.getString("nome"));
                        candidate.setParty(jsonCandidate.getString("partido"));
                        candidate.setImageUrl(jsonCandidate.getString("foto"));

                        candidateList.add(candidate);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            new GetImages().execute();
        }

    }

    private class GetImages extends AsyncTask<Void, Void, Void> {

        private URL url;
        private InputStream in;
        private Bitmap image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CandidateListActivity.this);
            pDialog.setMessage("Carregando Imagens...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            try {
                for (int i = 0; i < candidateList.size(); i++) {
                    downloadImage(candidateList.get(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void downloadImage(Candidate candidate) throws IOException {

            this.url = new URL(candidate.getImageUrl());
            this.in = this.url.openStream();
            this.image = BitmapFactory.decodeStream(this.in);

            this.in.close();

            candidate.setImage(this.image);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            ListCell adapter = new ListCell(CandidateListActivity.this, candidateList);

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Singleton.setDetail(candidateList.get(position));

                    Intent it = new Intent(CandidateListActivity.this, CandidateDetailActivity.class);
                    it.putExtra("type", type);
                    startActivity(it);
                }
            });
        }
    }
}
