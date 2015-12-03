package com.codepath.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.instagram.networking.InstagramClient;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Bind(R.id.mainVP) ViewPager mainVP;
    @Bind(R.id.mainTL) TabLayout mainTL;

    HomeFragmentStatePagerAdapter homeFragmentStatePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        homeFragmentStatePagerAdapter = new HomeFragmentStatePagerAdapter(getSupportFragmentManager(), this);
        mainVP.setAdapter(homeFragmentStatePagerAdapter);
        // Give the TabLayout the ViewPager
        mainTL.setupWithViewPager(mainVP);
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                InstagramClient.getInstance().clearAccessToken();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
