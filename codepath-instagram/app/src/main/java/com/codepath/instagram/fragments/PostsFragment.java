package com.codepath.instagram.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPostsWrapper;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.codepath.instagram.services.FetchHomeFeedService;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostsFragment extends BaseSwipeFragment {
    // `ip` = Instagram Post
    RecyclerView.LayoutManager ipLayoutManager;

    @Bind(R.id.ipRV)
    RecyclerView ipRecyclerView;

    InstagramPostsAdapter ipAdapter;

    ArrayList<InstagramPost> ipList;

    private BroadcastReceiver homeFeedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostsFragment.this.receivedBroadcast(intent);
        }
    };

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
    public void onResume() {
        super.onResume();

        // Listen to our fetch home feed service
        IntentFilter filter = new IntentFilter(FetchHomeFeedService.ACTION);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .registerReceiver(homeFeedReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(homeFeedReceiver);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSelfFeed();
    }

    public void receivedBroadcast(Intent intent) {
        swipeContainer.setRefreshing(false);

        int resultCode = intent.getIntExtra("resultCode", 0);

        if (resultCode == 504) {
            try {
                String errorString = intent.getStringExtra("resultValue");
                Snackbar.make(ipRecyclerView, errorString, Snackbar.LENGTH_SHORT).show();
            } catch (Exception e) {
                Snackbar.make(ipRecyclerView, "Error code 504 encountered", Snackbar.LENGTH_SHORT).show();
            }
            return;
        }

        if (resultCode != 200) {
            showFetchFeedFailureSnackbar();
            return;
        }

        InstagramPostsWrapper ipw = null;
        try {
            ipw = (InstagramPostsWrapper) intent.getSerializableExtra("resultValue");
        } catch (Exception e) {
            // Don't care
        }
        if (ipw == null) {
            showFetchFeedFailureSnackbar();
            return;
        }

        ipList.clear(); // clear existing items if needed
        ipList.addAll(ipw.getInstagramPostList());
        ipAdapter.notifyDataSetChanged();
    }


    private void getSelfFeed() {
        swipeContainer.setRefreshing(true);
        System.out.println("refreshing");
        Context c = getContext();
        final InstagramClientDatabase icd = InstagramClientDatabase.getInstance(c);

        if (!Utils.isNetworkAvailable(c)) {
            Snackbar.make(ipRecyclerView, "No internet connection", Snackbar.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
            // Always clear first
            ipList.clear();
            ipList.addAll(icd.getAllInstagramPosts());
            ipAdapter.notifyDataSetChanged();
            return;
        }
        System.out.println("Serviceeee");

        Intent i = new Intent(getActivity(), FetchHomeFeedService.class);
        i.putExtra("accessToken", MainApplication.getRestClient().getAccessToken());
        getActivity().startService(i);
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
