package com.codepath.instagram.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
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

public class PostsFragment extends BaseSwipeFragment {
    // `ip` = Instagram Post
    RecyclerView.LayoutManager ipLayoutManager;

    @Bind(R.id.ipRV) RecyclerView ipRecyclerView;

    InstagramPostsAdapter ipAdapter;

    ArrayList<InstagramPost> ipList;

    public static PostsFragment newInstance(Bundle args) {
        PostsFragment fragment = new PostsFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);

        ButterKnife.bind(this, view);

        setUpRecyclerView();
        setUpSwipeToRefresh(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSelfFeed();
    }


    private void getSelfFeed() {
        Context c = getContext();
        if (!Utils.isNetworkAvailable(c)) {
            Snackbar.make(ipRecyclerView, "Please make sure you have network connection", Snackbar.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
            return;
        }
        InstagramClient.getInstance().getSelfFeed(new JsonHttpResponseHandler() {
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                swipeContainer.setRefreshing(false);
                showFetchFeedFailureSnackbar();
            }
        });
    }

    private void setUpRecyclerView() {
        ipList = new ArrayList<>();
        ipAdapter = new InstagramPostsAdapter(getActivity(), ipList);
        ipLayoutManager = new LinearLayoutManager(getContext());
        ipRecyclerView.setLayoutManager(ipLayoutManager);
        ipRecyclerView.setAdapter(ipAdapter);
    }

    @Override
    protected void onSwipe() {
        getSelfFeed();
    }

    private void showFetchFeedFailureSnackbar() {
        final Snackbar sbFailed = Snackbar.make(ipRecyclerView, "Failed fetching feed", Snackbar.LENGTH_LONG);
        sbFailed.setAction("Refresh", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelfFeed();
                sbFailed.dismiss();
            }
        });
        sbFailed.show();
    }
}
