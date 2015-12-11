package com.codepath.twitterclient.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.twitterclient.R;
import com.codepath.twitterclient.activities.PostTweetActivity;
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

public class HomeTimelineFragment extends BaseSwipeFragment {
    @Bind(R.id.tweetRV) RecyclerView tweetRV;

    RecyclerView.LayoutManager layoutManager;

    private HomeTimelineAdapter homeTimelineAdapter;

    private ArrayList<Tweet> tweets;

    public static HomeTimelineFragment newInstance(Bundle args) {
        HomeTimelineFragment fragment = new HomeTimelineFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);
        ButterKnife.bind(this, view);

        setUpRecyclerView();
        setUpSwipeToRefresh(view);

        // First time
        fetchHomeTimeline(0, 0);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PostTweetActivity.REQUEST_CODE_POST_TWEET:
                if (resultCode == Activity.RESULT_OK) {
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
        homeTimelineAdapter = new HomeTimelineAdapter(getActivity(), tweets);
        layoutManager = new LinearLayoutManager(getActivity());
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
