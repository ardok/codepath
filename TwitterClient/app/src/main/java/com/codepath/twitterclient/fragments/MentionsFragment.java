package com.codepath.twitterclient.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.twitterclient.R;
import com.codepath.twitterclient.adapters.MentionTimelineAdapter;
import com.codepath.twitterclient.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.twitterclient.models.Mention;
import com.codepath.twitterclient.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MentionsFragment extends BaseSwipeFragment {
    @Bind(R.id.mentionRV) RecyclerView mentionRV;

    RecyclerView.LayoutManager layoutManager;

    private MentionTimelineAdapter mentionTimelineAdapter;

    private ArrayList<Mention> mentions;

    public static MentionsFragment newInstance(Bundle args) {
        MentionsFragment fragment = new MentionsFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mentions, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        setUpSwipeToRefresh(view);
        fetchMentionsTimeline(0, 0);
        return view;
    }

    private void setUpRecyclerView() {
        mentions = new ArrayList<>();
        mentionTimelineAdapter = new MentionTimelineAdapter(getActivity(), mentions);
        layoutManager = new LinearLayoutManager(getActivity());
        mentionRV.setLayoutManager(layoutManager);
        mentionRV.setAdapter(mentionTimelineAdapter);
        mentionRV.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchMentionsTimeline(page, Mention.getMentionWithLowestId(mentions).getUid());
            }
        });
    }

    private void fetchMentionsTimeline(final int page, long maxId) {
        swipeRefreshLayout.setRefreshing(true);
        TwitterClient.getInstance().getMentionsTimeline(page > 0 ? 26 : 25, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                swipeRefreshLayout.setRefreshing(false);
                if (page == 0) {
                    mentions.clear();
                }

                ArrayList<Mention> newMentions = Mention.fromJSONArray(response);

                if (page > 0) {
                    // Remove the first one since it's basically the last of the previous list
                    newMentions.remove(0);
                }

                mentions.addAll(newMentions);

                if (page == 0) {
                    mentionTimelineAdapter.notifyDataSetChanged();
                } else {
                    mentionTimelineAdapter.notifyItemRangeInserted(mentionTimelineAdapter.getItemCount(),
                            mentions.size() - 1);
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
        fetchMentionsTimeline(0, 0);
    }
}
