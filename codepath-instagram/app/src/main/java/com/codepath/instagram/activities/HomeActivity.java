package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseSwipeActivity {
    private static final String TAG = "HomeActivity";

    // `ip` = Instagram Post
    RecyclerView.LayoutManager ipLayoutManager;

    @Bind(R.id.ipRV) RecyclerView ipRecyclerView;

    InstagramPostsAdapter ipAdapter;

    ArrayList<InstagramPost> ipList;

    String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        clientId = ((MainApplication) getApplication()).getClientId();

        setUpRecyclerView();
        setUpSwipeToRefresh();
        getPopularFeed();
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

    private void getPopularFeed() {
        if (!Utils.isNetworkAvailable(this)) {
            Snackbar.make(ipRecyclerView, "Please make sure you have network connection", Snackbar.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
            return;
        }
        InstagramClient.getPopularFeed(clientId, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                swipeContainer.setRefreshing(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject meta = response.getJSONObject("meta");
                    if (meta.getInt("code") != 200) {
                        showFetchFeedFailureSnackbar();
                        return;
                    }
                    ipList.clear(); // clear existing items if needed
                    ipList.addAll(Utils.decodePostsFromJsonResponse(response));
                    ipAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    showFetchFeedFailureSnackbar();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                showFetchFeedFailureSnackbar();
            }
        });
    }

    private void setUpRecyclerView() {
        ipList = new ArrayList<>();
        ipAdapter = new InstagramPostsAdapter(HomeActivity.this, ipList, clientId);
        ipLayoutManager = new LinearLayoutManager(this);
        ipRecyclerView.setLayoutManager(ipLayoutManager);
        ipRecyclerView.setAdapter(ipAdapter);
    }

    @Override
    protected void onSwipe() {
        getPopularFeed();
    }

    private void showFetchFeedFailureSnackbar() {
        final Snackbar sbFailed = Snackbar.make(ipRecyclerView, "Failed fetching feed", Snackbar.LENGTH_LONG);
        sbFailed.setAction("Refresh", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPopularFeed();
                sbFailed.dismiss();
            }
        });
    }
}
