package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    // `ip` = Instagram Post
    RecyclerView.LayoutManager ipLayoutManager;
    RecyclerView ipRecyclerView;
    
    InstagramPostsAdapter ipAdapter;

    ArrayList<InstagramPost> ipList;

    JSONObject jsonPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ipRecyclerView = (RecyclerView) findViewById(R.id.ipRV);

        try {
            jsonPopular = Utils.loadJsonFromAsset(getApplicationContext(), "popular.json");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (jsonPopular != null) {
            setUpRecyclerView();
        } else {
            Toast.makeText(this, "No data found, sorry", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        ipList = (ArrayList<InstagramPost>) Utils.decodePostsFromJsonResponse(jsonPopular);
        ipAdapter = new InstagramPostsAdapter(ipList);
        ipLayoutManager = new LinearLayoutManager(this);
        ipRecyclerView.setLayoutManager(ipLayoutManager);
        ipRecyclerView.setAdapter(ipAdapter);
    }
}
