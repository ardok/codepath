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
import com.codepath.twitterclient.models.User;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int PROFILE_HEADER = 0;
    public static final int PROFILE_TWEET = 1;

    private User user;
    private ArrayList<Tweet> tweets;
    private Activity activity;

    public ProfileAdapter(Activity activity,
                          User user,
                          ArrayList<Tweet> tweets) {
        this.user = user;
        this.activity = activity;
        this.tweets = tweets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case PROFILE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_header, parent, false);
                return new ProfileHeaderViewHolder(view, activity);
            case PROFILE_TWEET:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mention_timeline, parent, false);
                return new ProfileTweetViewHolder(view, activity);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getLayoutPosition() == 0) {
            // Header
            ProfileHeaderViewHolder phvh = (ProfileHeaderViewHolder) holder;
            phvh.setProfileAvatar(user.getProfileImageUrl());
            phvh.profileFullNameTV.setText(user.getName());
            phvh.profileScreenNameTV.setText("@" + user.getScreenName());
            phvh.profileSummaryTV.setText(user.getDescription());
            phvh.setFollowersCount(user.getFollowersCount());
            phvh.setFollowingCount(user.getFriendsCount());
            return;
        }
         
        // Tweet
        final Tweet tweet = tweets.get(holder.getLayoutPosition() - 1);
        ProfileTweetViewHolder ptvh = (ProfileTweetViewHolder) holder;
        ptvh.setTweeterAvatar(tweet.getUser().getProfileImageUrl());
        ptvh.setTweeterFullName(tweet.getUser().getName());
        ptvh.setTweeterScreenName(tweet.getUser().getScreenName());
        ptvh.setTweetBody(tweet.getBody());
        ptvh.setTweetDate(tweet.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        // `+1` is for the header
        return tweets.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return PROFILE_HEADER;
            default:
                return PROFILE_TWEET;
        }
    }

    static class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {
        // Why is the ids are the same like tweet?
        // Because we're re-using the layout for now
        @Bind(R.id.tweeterAvatarIV) ImageView profileAvatarIV;
        @Bind(R.id.tweeterFullNameTV) TextView profileFullNameTV;
        @Bind(R.id.tweeterScreenNameTV) TextView profileScreenNameTV;
        @Bind(R.id.tweetBodyTV) TextView profileSummaryTV;
        @Bind(R.id.followersCountTV) TextView followersCountTV;
        @Bind(R.id.followingCountTV) TextView followingCountTV;
        
        public ProfileHeaderViewHolder(View itemView, final Activity activity) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setProfileAvatar(String url) {
            ImageUtils.setPicassoImage(profileAvatarIV, url);
        }

        public void setFollowersCount(long count) {
            followersCountTV.setText(String.format("%s Follower%s", count, count > 1 ? "s" : ""));
        }

        public void setFollowingCount(long count) {
            followingCountTV.setText(String.format("%s Follower%s", count, count > 1 ? "s" : ""));
        }
    }

    static class ProfileTweetViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tweeterAvatarIV) ImageView tweeterAvatarIV;
        @Bind(R.id.tweeterFullNameTV) TextView tweeterFullNameTV;
        @Bind(R.id.tweeterScreenNameTV) TextView tweeterScreenNameTV;
        @Bind(R.id.tweetBodyTV) TextView tweetBodyTV;
        @Bind(R.id.tweetDateTV) TextView tweetDateTV;

        public ProfileTweetViewHolder(View itemView, final Activity activity) {
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
