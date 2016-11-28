package com.example.cagri.votaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(Singleton.getElectorId() == null){
            Intent it = new Intent(this, AuthActivity.class);
            startActivity(it);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onNavigationItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent it = new Intent(this, CandidateListActivity.class);

        if (id == R.id.action_mayors) {
            it.putExtra("type","prefeito");
            startActivity(it);
        } else if (id == R.id.action_councilmen) {
            it.putExtra("type","vereador");
            startActivity(it);
        } else if (id == R.id.action_confirm) {
            it = new Intent(this, ConfirmActivity.class);
            startActivity(it);
        } else if (id == R.id.action_exit) {
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }

        return true;
    }
}
