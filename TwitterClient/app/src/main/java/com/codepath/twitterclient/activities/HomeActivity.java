package com.codepath.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.twitterclient.R;
import com.codepath.twitterclient.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.twitterclient.core.TwitterApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.mainVP) ViewPager mainVP;
    @Bind(R.id.mainTL) TabLayout mainTL;

    HomeFragmentStatePagerAdapter homeFragmentStatePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        homeFragmentStatePagerAdapter = new HomeFragmentStatePagerAdapter(getSupportFragmentManager(), this);
        mainVP.setAdapter(homeFragmentStatePagerAdapter);
        // Give the TabLayout the ViewPager
        mainTL.setupWithViewPager(mainVP);
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_tweet:
                intent = new Intent(HomeActivity.this, PostTweetActivity.class);
                startActivityForResult(intent, PostTweetActivity.REQUEST_CODE_POST_TWEET);
                return true;

            case R.id.action_profile:
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("user", TwitterApplication.getSessionUser());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
