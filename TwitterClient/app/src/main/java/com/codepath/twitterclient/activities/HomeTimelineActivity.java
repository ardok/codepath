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
import com.codepath.twitterclient.adapters.HomeTimelineAdapter;
import com.codepath.twitterclient.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.twitterclient.models.Tweet;
import com.codepath.twitterclient.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeTimelineActivity extends BaseSwipeActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tweetRV) RecyclerView tweetRV;

    RecyclerView.LayoutManager layoutManager;

    private HomeTimelineAdapter homeTimelineAdapter;

    private ArrayList<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setUpSwipeToRefresh();
        setUpRecyclerView();

        // First time
        fetchHomeTimeline(0, 0);
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_tweet:
                Intent intent = new Intent(HomeTimelineActivity.this, PostTweetActivity.class);
                startActivityForResult(intent, PostTweetActivity.REQUEST_CODE_POST_TWEET);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PostTweetActivity.REQUEST_CODE_POST_TWEET:
                if (resultCode == RESULT_OK) {
                    Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
                    // Tweet is already ordered from newest to oldest
                    tweets.add(0, newTweet);
                    homeTimelineAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void setUpRecyclerView() {
        tweets = new ArrayList<>();
        homeTimelineAdapter = new HomeTimelineAdapter(this, tweets);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        tweetRV.setLayoutManager(layoutManager);
        tweetRV.setAdapter(homeTimelineAdapter);
        tweetRV.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchHomeTimeline(page, Tweet.getTweetWithLowestId(tweets).getUid());
            }
        });
    }

    private void fetchHomeTimeline(final int page, long maxId) {
        swipeRefreshLayout.setRefreshing(true);
        TwitterClient.getInstance().getHomeTimeline(page > 0 ? 26 : 25, maxId, new JsonHttpResponseHandler() {
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
                    homeTimelineAdapter.notifyDataSetChanged();
                } else {
                    homeTimelineAdapter.notifyItemRangeInserted(homeTimelineAdapter.getItemCount(),
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

    @Override
    protected void onSwipe() {
        fetchHomeTimeline(0, 0);
    }
}
