package com.codepath.twitterclient.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.twitterclient.R;
import com.codepath.twitterclient.helpers.ImageUtils;
import com.codepath.twitterclient.helpers.StringUtils;
import com.codepath.twitterclient.models.Tweet;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeTimelineAdapter extends RecyclerView.Adapter<HomeTimelineAdapter.TweetTimelineViewHolder> {

    private ArrayList<Tweet> tweetList;
    private Activity activity;

    public HomeTimelineAdapter(Activity activity,
                               ArrayList<Tweet> tweetList) {
        this.activity = activity;
        this.tweetList = tweetList;
    }

    @Override
    public TweetTimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tweet_timeline, parent, false);
        return new TweetTimelineViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(TweetTimelineViewHolder holder, int position) {
        final Tweet tweet = tweetList.get(holder.getLayoutPosition());
        holder.setTweeterAvatar(tweet.getUser().getProfileImageUrl());
        holder.setTweeterFullName(tweet.getUser().getName());
        holder.setTweeterScreenName(tweet.getUser().getScreenName());
        holder.setTweetBody(tweet.getBody());
        holder.setTweetDate(tweet.getCreatedAt());
        System.out.println(tweet.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    static class TweetTimelineViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tweeterAvatarIV) ImageView tweeterAvatarIV;
        @Bind(R.id.tweeterFullNameTV) TextView tweeterFullNameTV;
        @Bind(R.id.tweeterScreenNameTV) TextView tweeterScreenNameTV;
        @Bind(R.id.tweetBodyTV) TextView tweetBodyTV;
        @Bind(R.id.tweetDateTV) TextView tweetDateTV;

        public TweetTimelineViewHolder(View itemView, final Activity activity) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTweeterAvatar(String url) {
            ImageUtils.setPicassoImage(tweeterAvatarIV, url);
        }

        public void setTweeterFullName(String name) {
            tweeterFullNameTV.setText(name);
        }

        public void setTweeterScreenName(String name) {
            tweeterScreenNameTV.setText(String.format("@%s", name));
        }

        public void setTweetBody(String tweetBody) {
            tweetBodyTV.setText(tweetBody);
        }

        public void setTweetDate(String rawDate) {
            tweetDateTV.setText(StringUtils.getRelativeTimeAgo(rawDate));
        }
    }
}
