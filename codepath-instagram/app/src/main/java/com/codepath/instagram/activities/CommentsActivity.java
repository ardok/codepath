package com.codepath.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramCommentsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommentsActivity extends BaseSwipeActivity {

    // `ic` = instagram comment
    RecyclerView.LayoutManager icLayoutManager;

    @Bind(R.id.icRV) RecyclerView icRecyclerView;

    InstagramCommentsAdapter icAdapter;

    ArrayList<InstagramComment> icList;

    String mediaId;
    String clientId;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            finishEarly();
            return;
        }

        clientId = intent.getStringExtra("clientId");
        mediaId = intent.getStringExtra("mediaId");
        code = intent.getIntExtra("code", 0);
        if (mediaId == null || clientId == null || code != 100) {
            finishEarly();
            return;
        }

        setUpSwipeToRefresh();
        setUpRecyclerView();

        getComments();
    }

    @Override
    protected void onSwipe() {
        getComments();
    }

    private void finishEarly() {
        Toast.makeText(this, "No data passed in", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getComments() {
        if (!Utils.isNetworkAvailable(this)) {
            Snackbar.make(icRecyclerView, "Please make sure you have network connection", Snackbar.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
            return;
        }
        InstagramClient.getComments(mediaId, clientId, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                swipeContainer.setRefreshing(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject meta = response.getJSONObject("meta");
                    if (meta.getInt("code") != 200) {
                        showFetchCommentsFailure();
                        return;
                    }
                    icList.clear(); // clear existing items if needed
                    icList.addAll(Utils.decodeCommentsFromJsonResponse(response));
                    icAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    showFetchCommentsFailure();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                showFetchCommentsFailure();
            }
        });
    }

    private void showFetchCommentsFailure() {
        final Snackbar sbFailed = Snackbar.make(icRecyclerView, "Failed fetching feed", Snackbar.LENGTH_LONG);
        sbFailed.setAction("Refresh", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getComments();
                sbFailed.dismiss();
            }
        });
    }

    private void setUpRecyclerView() {
        icList = new ArrayList<>();
        icAdapter = new InstagramCommentsAdapter(icList);
        icLayoutManager = new LinearLayoutManager(this);
        icRecyclerView.setLayoutManager(icLayoutManager);
        icRecyclerView.setAdapter(icAdapter);
    }
}
