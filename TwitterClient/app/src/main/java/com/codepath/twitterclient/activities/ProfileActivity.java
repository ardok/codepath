package com.codepath.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.twitterclient.R;
import com.codepath.twitterclient.adapters.ProfileAdapter;
import com.codepath.twitterclient.core.TwitterApplication;
import com.codepath.twitterclient.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.twitterclient.models.Tweet;
import com.codepath.twitterclient.models.User;
import com.codepath.twitterclient.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseSwipeActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Bind(R.id.tweetRV) RecyclerView tweetRV;

    RecyclerView.LayoutManager layoutManager;

    private ProfileAdapter profileAdapter;

    private ArrayList<Tweet> tweets;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpSwipeToRefresh();

        user = (User) intent.getSerializableExtra("user");
        if (user != null) {
            setUpRecyclerView();
            getSupportActionBar().setTitle("@" + user.getScreenName());
            fetchUserTimeline(0, 0);
            return;
        }

        // User is null, it means that we need to fetch that user's info first
        long userId = intent.getLongExtra("userId", 0);
        if (userId == 0) {
            finish();
            return;
        }
        fetchUser(userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpRecyclerView() {
        tweets = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this, user, tweets);
        layoutManager = new LinearLayoutManager(this);
        tweetRV.setLayoutManager(layoutManager);
        tweetRV.setAdapter(profileAdapter);
        tweetRV.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchUserTimeline(page, Tweet.getTweetWithLowestId(tweets).getUid());
            }
        });
    }

    private void fetchUserTimeline(final int page, long maxId) {
        swipeRefreshLayout.setRefreshing(true);
        TwitterClient.getInstance().getUserTimeline(user.getUid(), page > 0 ? 26 : 25, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                swipeRefreshLayout.setRefreshing(false);
                if (page == 0) {
                    tweets.clear();
                }

                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);

                if (page > 0) {
                    // Remove the first one since it's basically the last of the previous list
                    newTweets.remove(0);
                }

                tweets.addAll(newTweets);

                if (page == 0) {
                    profileAdapter.notifyDataSetChanged();
                } else {
                    profileAdapter.notifyItemRangeInserted(profileAdapter.getItemCount(),
                            tweets.size() - 1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeRefreshLayout.setRefreshing(false);
                if (errorResponse != null) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }
        });
    }

    private void fetchUser(long userId) {
        swipeRefreshLayout.setRefreshing(true);
        TwitterClient.getInstance().getUser(userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                // We have user, now set everything up and fetch timeline
                setUpRecyclerView();
                getSupportActionBar().setTitle("@" + user.getScreenName());
                fetchUserTimeline(0, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeRefreshLayout.setRefreshing(false);
                if (errorResponse != null) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }
        });
    }

    @Override
    protected void onSwipe() {
        fetchUser(user.getUid());
    }
}
